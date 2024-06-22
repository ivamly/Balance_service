package com.ivmaly.transaction.models;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {

    @Test
    public void testSetBalance_NullBalance_ThrowsException() {
        // Arrange
        User user = new User();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> user.setBalance(null));
    }

    @Test
    public void testSetBalance_NegativeBalance_ThrowsException() {
        // Arrange
        User user = new User();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> user.setBalance(BigDecimal.valueOf(-10)));
    }

    @Test
    public void testSetBalance_ValidBalance_SetsBalance() {
        // Arrange
        User user = new User();
        BigDecimal expectedBalance = BigDecimal.TEN;

        // Act
        user.setBalance(expectedBalance);

        // Assert
        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    public void testConstructor_InitializesBalanceToZero() {
        // Arrange & Act
        User user = new User();

        // Assert
        assertEquals(BigDecimal.ZERO, user.getBalance());
    }

    @Test
    public void testGetUserId_AfterSettingId_ReturnsCorrectId() {
        // Arrange
        User user = new User();
        Long expectedId = 1L;

        // Act
        user.setUserId(expectedId);

        // Assert
        assertEquals(expectedId, user.getUserId());
    }
}

