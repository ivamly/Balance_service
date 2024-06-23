package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Autowired
    public UserService(UserRepository userRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    public BigDecimal getUserBalance(Long userId) {
        return userRepository.findById(userId)
                .map(User::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);
        userRepository.save(user);
        transactionService.createTransactionWithouCounterpary(user, amount, "DEPOSIT", "NONE");
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        BigDecimal newBalance = user.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setBalance(newBalance);
        userRepository.save(user);
        transactionService.createTransactionWithouCounterpary(user, amount, "WITHDRAW", "NONE");
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
