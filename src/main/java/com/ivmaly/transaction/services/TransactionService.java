package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createTransactionWithCounterpary(User user, User counterparty, BigDecimal amount, String service, String order) {
        // Проверка на допустимость входных данных
        if (user == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid input data");
        }
        Transaction transaction = new Transaction(user, counterparty, amount, service, order);

        transactionRepository.save(transaction);
    }

    @Transactional
    public void createTransactionWithouCounterpary(User user, BigDecimal amount, String service, String order) {
        // Проверка на допустимость входных данных
        if (user == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid input data");
        }
        Transaction transaction = new Transaction(user, amount, service, order);


        transactionRepository.save(transaction);

    }
}
