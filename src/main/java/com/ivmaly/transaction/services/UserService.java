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

    @Transactional
    public void transfer(Long userId, Long counterpartyId, BigDecimal amount) {
        validateInput(userId, counterpartyId, amount);

        User user = getUserById(userId);
        User counterpartyUser = getUserById(counterpartyId);

        if (counterpartyUser.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Counterparty has insufficient funds");
        }

        counterpartyUser.setBalance(counterpartyUser.getBalance().subtract(amount));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        userRepository.save(counterpartyUser);

        transactionService.createTransaction(user, counterpartyUser, amount, "TRANSFER", "NONE");
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        validateInput(userId, amount);

        User user = getUserById(userId);
        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);
        userRepository.save(user);

        transactionService.createTransaction(user, null, amount, "DEPOSIT", "NONE");
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        validateInput(userId, amount);

        User user = getUserById(userId);
        if (user.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        transactionService.createTransaction(user, null, amount, "WITHDRAW", "NONE");
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public BigDecimal getUserBalance(Long userId) {
        return userRepository.findById(userId)
                .map(User::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void validateInput(Long userId, BigDecimal amount) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
    }

    private void validateInput(Long userId, Long counterpartyId, BigDecimal amount) {
        validateInput(userId, amount);
        if (counterpartyId == null || counterpartyId <= 0 || !userRepository.existsById(counterpartyId)) {
            throw new IllegalArgumentException("Invalid counterparty ID");
        }
    }
}
