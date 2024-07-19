package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;

import java.math.BigDecimal;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(BigDecimal availableBalance) throws IllegalArgumentException {
        validateNonNegative(availableBalance, "Available balance");
        User user = new User(availableBalance);
        userRepository.save(user);
    }

    public void createUser(BigDecimal availableBalance, BigDecimal reservedBalance) throws IllegalArgumentException {
        validateNonNegative(availableBalance, "Available balance");
        validateNonNegative(reservedBalance, "Reserved balance");
        User user = new User(availableBalance, reservedBalance);
        userRepository.save(user);
    }

    public User getUserById(Long userId) throws IllegalArgumentException {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found"));
    }

    private void validateNonNegative(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
    }
}