package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAvailableBalance(BigDecimal.valueOf(1000));
        user.setReservedBalance(BigDecimal.valueOf(200));
    }

    @Test
    void testReserveSuccess() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.reserve(userId, amount);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(BigDecimal.valueOf(900), savedUser.getAvailableBalance());
        assertEquals(BigDecimal.valueOf(300), savedUser.getReservedBalance());
    }

    @Test
    void testReserveInsufficientFunds() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(1200);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.reserve(userId, amount));
    }

    @Test
    void testDepositSuccess() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(500);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deposit(userId, amount);
        verify(userRepository).save(userCaptor.capture());
        verify(transactionService).createTransaction(user, amount, "DEPOSIT", "NONE");
        User savedUser = userCaptor.getValue();
        assertEquals(BigDecimal.valueOf(1500), savedUser.getAvailableBalance());
    }

    @Test
    void testWithdrawSuccess() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "testService";
        String order = "testOrder";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.withdraw(userId, amount, service, order);
        verify(userRepository).save(userCaptor.capture());
        verify(transactionService).createTransaction(user, amount, service, order);
        User savedUser = userCaptor.getValue();
        assertEquals(BigDecimal.valueOf(100), savedUser.getReservedBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(300);
        String service = "testService";
        String order = "testOrder";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.withdraw(userId, amount, service, order));
    }

    @Test
    void testGetAvailableBalance() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        BigDecimal availableBalance = userService.getAvailableBalance(userId);
        assertEquals(BigDecimal.valueOf(1000), availableBalance);
    }

    @Test
    void testGetReservedBalance() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        BigDecimal reservedBalance = userService.getReservedBalance(userId);
        assertEquals(BigDecimal.valueOf(200), reservedBalance);
    }

    @Test
    void testGetUserByIdUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getAvailableBalance(userId));
    }
}
