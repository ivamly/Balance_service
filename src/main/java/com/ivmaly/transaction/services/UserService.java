package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(BigDecimal availableBalance) throws IllegalArgumentException {
        validateNonNegative(availableBalance, "Available balance");
        User user = new User(availableBalance);
        userRepository.save(user);
        logger.info("Created user with available balance {}", availableBalance);
    }

    public void createUser(BigDecimal availableBalance, BigDecimal reservedBalance) throws IllegalArgumentException {
        validateNonNegative(availableBalance, "Available balance");
        validateNonNegative(reservedBalance, "Reserved balance");
        User user = new User(availableBalance, reservedBalance);
        userRepository.save(user);
        logger.info("Created user with available balance {} and reserved balance {}", availableBalance, reservedBalance);
    }

    public User getUserById(Long userId) throws IllegalArgumentException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return userOpt.get();
    }

    public void depositUser(Long userId, BigDecimal amount) throws IllegalArgumentException {
        validateNonNegative(amount, "Amount");
        User user = getUserById(userId);
        user.setAvailableBalance(user.getAvailableBalance().add(amount));
        userRepository.save(user);
        logger.info("Deposited {} to user with ID {}", amount, userId);
    }

    public void withdrawUser(Long userId, BigDecimal amount) throws IllegalArgumentException {
        validateNonNegative(amount, "Amount");
        User user = getUserById(userId);
        user.setAvailableBalance(user.getAvailableBalance().subtract(amount));
        userRepository.save(user);
        logger.info("Withdrew {} from user with ID {}", amount, userId);
    }

    public void updateUser(User user) {
        userRepository.save(user);
        logger.info("Updated user with ID {}", user.getUserId());
    }

    private void validateNonNegative(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
    }
}
