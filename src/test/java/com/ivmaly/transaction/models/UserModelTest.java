package com.ivmaly.transaction.models;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {

    @Test
    public void testDefaultConstructor() {
        User user = new User();

        assertNotNull(user);
    }

    @Test
    public void testDefaultConstructor_SetsDefaultBalance() {
        User user = new User();

        assertEquals(BigDecimal.ZERO, user.getBalance());
    }

    @Test
    public void testDefaultConstructor_GeneratesNullId() {
        User user = new User();

        assertNull(user.getUserId());
    }

    @Test
    public void testConstructorWithArg() {
        User user = new User(BigDecimal.ONE);

        assertNotNull(user);
    }

    @Test
    public void testConstructorWithArg_SetsBalance() {
        BigDecimal balance = new BigDecimal("100.00");
        User user = new User(balance);

        assertEquals(balance, user.getBalance());
    }

    @Test
    public void testConstructorWithArg_GeneratesNullId() {
        BigDecimal balance = new BigDecimal("100.00");
        User user = new User(balance);

        assertNull(user.getUserId());
    }

    @Test
    public void testSetBalance_NullBalance_ThrowsException() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setBalance(null));
    }

    @Test
    public void testSetBalance_NegativeBalance_ThrowsException() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setBalance(BigDecimal.valueOf(-10)));
    }

    @Test
    public void testSetBalance_ValidBalance_SetsBalance() {
        User user = new User();
        BigDecimal expectedBalance = BigDecimal.TEN;

        user.setBalance(expectedBalance);

        assertEquals(expectedBalance, user.getBalance());
    }
}

