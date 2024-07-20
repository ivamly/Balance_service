package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public Transaction createDeposit(Long userId, BigDecimal amount) {
        userService.depositUser(userId, amount);
        User user = userService.getUserById(userId);
        Transaction transaction = Transaction.createDepositTransaction(user, amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public Transaction createWithdraw(Long userId, BigDecimal amount) {
        userService.withdrawUser(userId, amount);
        User user = userService.getUserById(userId);
        Transaction transaction = Transaction.createWithdrawalTransaction(user, amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public List<Transaction> createTransfer(Long userFromId, Long userToId, BigDecimal amount) {
        List<Transaction> transactions = new ArrayList<>();
        userService.withdrawUser(userFromId, amount);
        User userFrom = userService.getUserById(userFromId);
        Transaction transactionW = Transaction.createWithdrawalTransaction(userFrom, amount);
        transactionRepository.save(transactionW);
        userService.depositUser(userToId, amount);
        User userTo = userService.getUserById(userToId);
        Transaction transactionD = Transaction.createDepositTransaction(userTo, amount);
        transactionRepository.save(transactionD);
        transactions.add(transactionW);
        transactions.add(transactionD);
        return transactions;
    }
}
