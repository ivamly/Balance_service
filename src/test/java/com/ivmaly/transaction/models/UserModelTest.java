package com.ivmaly.transaction.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testDefaultConstructorAndGetters() {
        User user = new User();
        assertNull(user.getUserId());
        assertEquals(BigDecimal.ZERO, user.getAvailableBalance());
        assertEquals(BigDecimal.ZERO, user.getReservedBalance());
    }

    @Test
    void testSetAndGetUserId() {
        User user = new User();
        Long userId = 1L;
        user.setUserId(userId);
        assertEquals(userId, user.getUserId());
    }

    @Test
    void testSetAndGetAvailableBalance() {
        User user = new User();
        BigDecimal availableBalance = BigDecimal.valueOf(100);
        user.setAvailableBalance(availableBalance);
        assertEquals(availableBalance, user.getAvailableBalance());
    }

    @Test
    void testSetAvailableBalanceNegative() {
        User user = new User();
        BigDecimal negativeBalance = BigDecimal.valueOf(-100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                user.setAvailableBalance(negativeBalance));
        assertEquals("The available balance cannot be negative", exception.getMessage());
    }

    @Test
    void testSetAndGetReservedBalance() {
        User user = new User();
        BigDecimal reservedBalance = BigDecimal.valueOf(50);
        user.setReservedBalance(reservedBalance);
        assertEquals(reservedBalance, user.getReservedBalance());
    }

    @Test
    void testSetReservedBalanceNegative() {
        User user = new User();
        BigDecimal negativeBalance = BigDecimal.valueOf(-50);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                user.setReservedBalance(negativeBalance));
        assertEquals("The reserved balance cannot be negative", exception.getMessage());
    }
}
