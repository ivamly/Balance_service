package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Autowired
    public UserService(UserRepository userRepository, TransactionService transactionService) {
        if (userRepository == null || transactionService == null) {
            throw new IllegalArgumentException("UserRepository and TransactionService must not be null");
        }
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public void reserve(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        if (user.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available funds");
        }
        user.setAvailableBalance(user.getAvailableBalance().subtract(amount));
        user.setReservedBalance(user.getReservedBalance().add(amount));
        userRepository.save(user);
    }

    @Transactional
    public void undoReserve(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        if (user.getReservedBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient reserved funds");
        }
        user.setReservedBalance(user.getReservedBalance().subtract(amount));
        user.setAvailableBalance(user.getAvailableBalance().add(amount));
        userRepository.save(user);
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        user.setAvailableBalance(user.getAvailableBalance().add(amount));
        userRepository.save(user);
        transactionService.createTransaction(user, amount, "DEPOSIT", "NONE");
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount, String service, String order) {
        User user = getUserById(userId);
        if (user.getReservedBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient reserved funds");
        }
        user.setReservedBalance(user.getReservedBalance().subtract(amount));
        userRepository.save(user);
        transactionService.createTransaction(user, amount, service, order);
    }

    public BigDecimal getAvailableBalance(Long userId) {
        User user = getUserById(userId);
        return user.getAvailableBalance();
    }

    public BigDecimal getReservedBalance(Long userId) {
        User user = getUserById(userId);
        return user.getReservedBalance();
    }

    private User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
