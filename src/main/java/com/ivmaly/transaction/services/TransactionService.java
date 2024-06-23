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
    public void createTransaction(User user, User counterparty, BigDecimal amount, String service, String order) {
        validateInput(user, amount, service, order);
        Transaction transaction = new Transaction(user, counterparty, amount, service, order);
        transactionRepository.save(transaction);
    }

    private void validateInput(User user, BigDecimal amount, String service, String order) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (service == null) {
            throw new IllegalArgumentException("Service must not be null");
        }
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

    }
}
