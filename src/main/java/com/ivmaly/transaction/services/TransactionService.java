package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Balance;
import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.TransactionType;
import com.ivmaly.transaction.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, UserService userService, BalanceService balanceService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.balanceService = balanceService;
    }

    @Transactional
    public Transaction deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        Balance balance = balanceService.getBalanceByUserId(userId);
        BigDecimal newAvailableBalance = balance.getAvailableBalance().add(amount);
        balanceService.updateAvailableBalance(userId, newAvailableBalance);
        Transaction transaction = new Transaction(userService.getUserById(userId), amount, 1L, 1L, TransactionType.DEPOSIT);
        logger.info("Deposit transaction completed: User ID {}, Amount {}", userId, amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public Transaction withdraw(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        Balance balance = balanceService.getBalanceByUserId(userId);
        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        BigDecimal newAvailableBalance = balance.getAvailableBalance().subtract(amount);
        balanceService.updateAvailableBalance(userId, newAvailableBalance);
        Transaction transaction = new Transaction(userService.getUserById(userId), amount, -1L, -1L, TransactionType.WITHDRAWAL);
        logger.info("Withdrawal transaction completed: User ID {}, Amount {}", userId, amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal amount) {
        if (userIdFrom.equals(userIdTo)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }
        withdraw(userIdFrom, amount);
        deposit(userIdTo, amount);
        logger.info("Transfer transaction completed: From User ID {}, To User ID {}, Amount {}", userIdFrom, userIdTo, amount);
    }
}
