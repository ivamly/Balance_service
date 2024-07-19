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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_withPositiveBalance_createsUser() {
        BigDecimal balance = new BigDecimal("100.00");

        userService.createUser(balance);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_withNegativeBalance_throwsException() {
        BigDecimal balance = new BigDecimal("-100.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(balance));

        assertEquals("Available balance cannot be negative", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_withPositiveAvailableAndReservedBalance_createsUser() {
        BigDecimal availableBalance = new BigDecimal("100.00");
        BigDecimal reservedBalance = new BigDecimal("50.00");

        userService.createUser(availableBalance, reservedBalance);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_withNegativeReservedBalance_throwsException() {
        BigDecimal availableBalance = new BigDecimal("100.00");
        BigDecimal reservedBalance = new BigDecimal("-50.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(availableBalance, reservedBalance));

        assertEquals("Reserved balance cannot be negative", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_existingUser_returnsUser() {
        Long userId = 1L;
        User user = new User(BigDecimal.ZERO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_nonExistingUser_throwsException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_savesUser() {
        User user = new User(BigDecimal.ZERO);

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }
}
