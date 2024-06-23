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

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeposit_UserNotFound_ThrowsException() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(userRepository.existsById(userId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deposit(userId, amount);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testWithdraw_UserNotFound_ThrowsException() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(userRepository.existsById(userId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.withdraw(userId, amount);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testWithdraw_InsufficientFunds_ThrowsException() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        User user = new User();
        user.setUserId(userId);
        user.setBalance(BigDecimal.valueOf(50));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.withdraw(userId, amount);
        });
        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    public void testTransfer_UserNotFound_ThrowsException() {
        Long userId = 1L;
        Long counterpartyId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(userRepository.existsById(userId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.transfer(userId, counterpartyId, amount);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testTransfer_CounterpartyNotFound_ThrowsException() {
        Long userId = 1L;
        Long counterpartyId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);
        User user = new User();
        user.setUserId(userId);
        user.setBalance(BigDecimal.valueOf(1000));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsById(counterpartyId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.transfer(userId, counterpartyId, amount);
        });
        assertEquals("Invalid counterparty ID", exception.getMessage());
    }

    @Test
    public void testTransfer_CounterpartyInsufficientFunds_ThrowsException() {
        Long userId = 1L;
        Long counterpartyId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);
        User user = new User();
        user.setUserId(userId);
        user.setBalance(BigDecimal.valueOf(1000));
        User counterparty = new User();
        counterparty.setUserId(counterpartyId);
        counterparty.setBalance(BigDecimal.valueOf(50));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsById(counterpartyId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(counterpartyId)).thenReturn(Optional.of(counterparty));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.transfer(userId, counterpartyId, amount);
        });
        assertEquals("Counterparty has insufficient funds", exception.getMessage());
    }

    @Test
    public void testGetUserBalance_UserNotFound_ThrowsException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserBalance(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

}
