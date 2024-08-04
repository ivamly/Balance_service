package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.*;
import com.ivmaly.transaction.repositories.ReserveRepository;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ReserveService {
    private final ReserveRepository reserveRepository;
    private final BalanceService balanceService;
    private final TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReserveService.class);
    private final UserService userService;

    public ReserveService(ReserveRepository reserveRepository, BalanceService balanceService,
                          TransactionRepository transactionRepository, UserService userService) {
        this.reserveRepository = reserveRepository;
        this.balanceService = balanceService;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public void createReserve(Long userId, BigDecimal amount, Long serviceId, Long orderId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        Balance balance = balanceService.getBalanceByUserId(userId);

        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available balance");
        }

        BigDecimal newAvailableBalance = balance.getAvailableBalance().subtract(amount);
        BigDecimal newReservedBalance = balance.getReservedBalance().add(amount);

        balanceService.updateAvailableBalance(userId, newAvailableBalance);
        balanceService.updateReservedBalance(userId, newReservedBalance);

        Reserve reserve = new Reserve(userService.getUserById(userId), amount, serviceId, orderId);
        reserveRepository.save(reserve);
        logger.info("Reserve created: User ID {}, Amount {}", userId, amount);
    }

    @Transactional
    public void undoReserve(Long reserveId) {
        Reserve reserve = getReserveById(reserveId);
        if (reserve.getReserveStatus() != ReserveStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Insufficient reserve status");
        }

        Balance balance = balanceService.getBalanceByUserId(reserve.getUser().getUserId());
        BigDecimal newReservedBalance = balance.getReservedBalance().subtract(reserve.getReserveAmount());
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(reserve.getReserveAmount());

        balanceService.updateReservedBalance(reserve.getUser().getUserId(), newReservedBalance);
        balanceService.updateAvailableBalance(reserve.getUser().getUserId(), newAvailableBalance);

        reserve.setReserveStatus(ReserveStatus.CANCELLED);
        reserveRepository.save(reserve);
        logger.info("Reserve cancelled: Reserve ID {}", reserveId);
    }

    @Transactional
    public void completeReserve(Long reserveId) {
        Reserve reserve = getReserveById(reserveId);
        if (reserve.getReserveStatus() != ReserveStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Insufficient reserve status");
        }

        Balance balance = balanceService.getBalanceByUserId(reserve.getUser().getUserId());
        if (balance.getReservedBalance().compareTo(reserve.getReserveAmount()) < 0) {
            throw new IllegalArgumentException("Reserve amount exceeds reserved balance");
        }

        BigDecimal newReservedBalance = balance.getReservedBalance().subtract(reserve.getReserveAmount());
        balanceService.updateReservedBalance(reserve.getUser().getUserId(), newReservedBalance);
        Transaction transaction = new Transaction(reserve.getUser(), reserve.getReserveAmount(),
                reserve.getServiceId(), reserve.getOrderId(), TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);

        reserve.setReserveStatus(ReserveStatus.COMPLETED);
        reserveRepository.save(reserve);
        logger.info("Reserve completed: Reserve ID {}", reserveId);
    }

    public Reserve getReserveById(Long reserveId) {
        return reserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException("Reserve with id " + reserveId + " not found"));
    }
}
