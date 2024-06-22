package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createTransaction(User user, User counterparty, BigDecimal amount, String service, String order) {
        // Проверка на допустимость входных данных
        if (user == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid input data");
        }
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCounterparty(counterparty);
        transaction.setAmount(amount);
        transaction.setService(service);
        transaction.setOrderDescription(order);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

    }
}
