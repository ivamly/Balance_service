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
    private final UserService userService;
    private final BalanceService balanceService;
    private final TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReserveService.class);

    public ReserveService(ReserveRepository reserveRepository, UserService userService,
                          BalanceService balanceService, TransactionRepository transactionRepository) {
        this.reserveRepository = reserveRepository;
        this.userService = userService;
        this.balanceService = balanceService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createReserve(Long userId, BigDecimal amount, Long serviceId, Long orderId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User user = userService.getUserById(userId);
        Balance balance = balanceService.getBalanceByUser(user);

        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available balance");
        }

        BigDecimal newAvailableBalance = balance.getAvailableBalance().subtract(amount);
        BigDecimal newReservedBalance = balance.getReservedBalance().add(amount);

        balanceService.updateAvailableBalance(balance, newAvailableBalance);
        balanceService.updateReservedBalance(balance, newReservedBalance);

        Reserve reserve = new Reserve(user, amount, serviceId, orderId);
        reserveRepository.save(reserve);
        logger.info("Reserve created: User ID {}, Amount {}", userId, amount);
    }

    @Transactional
    public void undoReserve(Long reserveId) {
        Reserve reserve = getReserveById(reserveId);
        if (reserve.getReserveStatus() != ReserveStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Insufficient reserve status");
        }

        Balance balance = balanceService.getBalanceByUser(reserve.getUser());
        BigDecimal newReservedBalance = balance.getReservedBalance().subtract(reserve.getReserveAmount());
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(reserve.getReserveAmount());

        balanceService.updateReservedBalance(balance, newReservedBalance);
        balanceService.updateAvailableBalance(balance, newAvailableBalance);

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

        Balance balance = balanceService.getBalanceByUser(reserve.getUser());
        if (balance.getReservedBalance().compareTo(reserve.getReserveAmount()) < 0) {
            throw new IllegalArgumentException("Reserve amount exceeds reserved balance");
        }

        BigDecimal newReservedBalance = balance.getReservedBalance().subtract(reserve.getReserveAmount());
        balanceService.updateReservedBalance(balance, newReservedBalance);
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
