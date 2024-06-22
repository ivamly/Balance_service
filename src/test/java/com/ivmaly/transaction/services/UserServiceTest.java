package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserBalance_UserExists_ReturnsBalance() {
        // Arrange
        long userId = 1L;
        BigDecimal expectedBalance = BigDecimal.valueOf(100);
        User user = new User();
        user.setUserId(userId);
        user.setBalance(expectedBalance);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        BigDecimal actualBalance = userService.getUserBalance(userId);

        // Assert
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void testDeposit_ValidData_DepositsAmount() {
        // Arrange
        long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal depositAmount = BigDecimal.TEN;
        User user = new User();
        user.setUserId(userId);
        user.setBalance(initialBalance);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.deposit(userId, depositAmount);

        // Assert
        BigDecimal expectedBalance = initialBalance.add(depositAmount);
        assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).save(user);
        verify(transactionService, times(1)).createTransaction(user, null, depositAmount, "DEPOSIT", "NONE");
    }

    @Test
    public void testWithdraw_ValidData_WithdrawsAmount() {
        // Arrange
        long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withdrawAmount = BigDecimal.TEN;
        User user = new User();
        user.setUserId(userId);
        user.setBalance(initialBalance);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.withdraw(userId, withdrawAmount);

        // Assert
        BigDecimal expectedBalance = initialBalance.subtract(withdrawAmount);
        assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).save(user);
        verify(transactionService, times(1)).createTransaction(user, null, withdrawAmount, "WITHDRAW", "NONE");
    }

    // Add more tests to cover edge cases and error scenarios
}
