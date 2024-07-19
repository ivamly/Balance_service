package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserWithValidAvailableBalance() {
        BigDecimal availableBalance = BigDecimal.valueOf(100.00);

        userService.createUser(availableBalance);

        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void testCreateUserWithValidAvailableAndReservedBalance() {
        BigDecimal availableBalance = BigDecimal.valueOf(100.00);
        BigDecimal reservedBalance = BigDecimal.valueOf(50.00);

        userService.createUser(availableBalance, reservedBalance);

        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void testCreateUserWithNegativeAvailableBalance() {
        BigDecimal availableBalance = BigDecimal.valueOf(-100.00);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(availableBalance));

        assertEquals("Available balance cannot be negative", thrown.getMessage());
    }

    @Test
    public void testCreateUserWithNegativeReservedBalance() {
        BigDecimal availableBalance = BigDecimal.valueOf(100.00);
        BigDecimal reservedBalance = BigDecimal.valueOf(-50.00);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(availableBalance, reservedBalance));

        assertEquals("Reserved balance cannot be negative", thrown.getMessage());
    }

    @Test
    public void testGetUserByIdWithExistingId() {
        Long userId = 1L;
        User user = new User(BigDecimal.valueOf(100.00));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdWithNonExistingId() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                userService.getUserById(userId));

        assertEquals("User not found", thrown.getMessage());
    }
}
