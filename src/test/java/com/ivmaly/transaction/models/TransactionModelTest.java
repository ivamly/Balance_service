package com.ivmaly.transaction.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testConstructorAndGetters() {
        User user = new User();
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "testService";
        String orderDescription = "testOrder";
        LocalDateTime beforeCreation = LocalDateTime.now();
        Transaction transaction = new Transaction(user, amount, service, orderDescription);
        assertEquals(user, transaction.getUser());
        assertEquals(amount, transaction.getAmount());
        assertEquals(service, transaction.getService());
        assertEquals(orderDescription, transaction.getOrderDescription());
        assertTrue(transaction.getTimestamp().isAfter(beforeCreation));
    }

    @Test
    void testSettersAndGetters() {
        Transaction transaction = new Transaction();
        User user = new User();
        BigDecimal amount = BigDecimal.valueOf(200);
        String service = "anotherService";
        String orderDescription = "anotherOrder";
        LocalDateTime timestamp = LocalDateTime.now().minusDays(1);
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setService(service);
        transaction.setOrderDescription(orderDescription);
        transaction.setTimestamp(timestamp);
        assertEquals(user, transaction.getUser());
        assertEquals(amount, transaction.getAmount());
        assertEquals(service, transaction.getService());
        assertEquals(orderDescription, transaction.getOrderDescription());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testSetAmountNegative() {
        Transaction transaction = new Transaction();
        BigDecimal negativeAmount = BigDecimal.valueOf(-100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transaction.setAmount(negativeAmount));
        assertEquals("Amount cannot be negative", exception.getMessage());
    }

    @Test
    void testDefaultConstructor() {
        Transaction transaction = new Transaction();
        assertNull(transaction.getUser());
        assertNull(transaction.getAmount());
        assertNull(transaction.getService());
        assertNull(transaction.getOrderDescription());
        assertNotNull(transaction.getTimestamp());
    }
}
