package com.ivmaly.transaction.models;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionModelTest {

    @Test
    public void testDefaultConstructor() {
        Transaction transaction = new Transaction();

        assertNotNull(transaction);
    }

    @Test
    public void testSetAmount_NullAmount_ThrowsException() {
        Transaction transaction = new Transaction();

        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(null));
    }

    @Test
    public void testSetAmount_NegativeAmount_ThrowsException() {
        Transaction transaction = new Transaction();

        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(BigDecimal.valueOf(-10)));
    }

    @Test
    public void testSetService_NullService_ThrowsException() {
        Transaction transaction = new Transaction();

        assertThrows(IllegalArgumentException.class, () -> transaction.setService(null));
    }

    @Test
    public void testSetOrderDescription_NullOrderDescription_ThrowsException() {
        Transaction transaction = new Transaction();

        assertThrows(IllegalArgumentException.class, () -> transaction.setOrderDescription(null));
    }

    @Test
    public void testSetTimestamp_NullTimestamp_ThrowsException() {
        Transaction transaction = new Transaction();

        assertThrows(IllegalArgumentException.class, () -> transaction.setTimestamp(null));
    }
}
