package com.ivmaly.transaction.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void testUserConstructorWithTwoParameters() {
        User user = new User(new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertEquals(new BigDecimal("100.00"), user.getAvailableBalance());
        assertEquals(new BigDecimal("50.00"), user.getReservedBalance());
    }

    @Test
    void testUserConstructorWithOneParameter() {
        User user = new User(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), user.getAvailableBalance());
        assertEquals(BigDecimal.ZERO, user.getReservedBalance());
    }

    @Test
    void testSetAvailableBalance() {
        User user = new User(new BigDecimal("100.00"));
        user.setAvailableBalance(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), user.getAvailableBalance());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                user.setAvailableBalance(new BigDecimal("-50.00")));
        assertEquals("The available balance cannot be negative", exception.getMessage());
    }

    @Test
    void testSetReservedBalance() {
        User user = new User(new BigDecimal("100.00"));
        user.setReservedBalance(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("30.00"), user.getReservedBalance());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                user.setReservedBalance(new BigDecimal("-10.00")));
        assertEquals("The reserved balance cannot be negative", exception.getMessage());
    }

    @Test
    void testToString() {
        User user = new User(new BigDecimal("100.00"), new BigDecimal("50.00"));
        String expected = "User{userId=null, availableBalance=100.00, reservedBalance=50.00}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User(new BigDecimal("100.00"), new BigDecimal("50.00"));
        User user2 = new User(new BigDecimal("100.00"), new BigDecimal("50.00"));
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
