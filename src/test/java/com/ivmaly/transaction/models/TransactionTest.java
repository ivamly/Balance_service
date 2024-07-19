package com.ivmaly.transaction.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = Mockito.mock(User.class);
    }

    @Test
    void testTransactionConstructorWithTwoParameters() {
        Transaction transaction = new Transaction(user, new BigDecimal("100.00"));
        assertEquals(user, transaction.getUser());
        assertEquals(new BigDecimal("100.00"), transaction.getTransactionAmount());
        assertEquals(0L, transaction.getServiceId());
        assertEquals(0L, transaction.getOrderId());
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertNotNull(transaction.getTransactionDateTime());
    }

    @Test
    void testTransactionConstructorWithReserve() {
        Reserve reserve = Mockito.mock(Reserve.class);
        Mockito.when(reserve.getUser()).thenReturn(user);
        Mockito.when(reserve.getReserveAmount()).thenReturn(new BigDecimal("50.00"));
        Mockito.when(reserve.getServiceId()).thenReturn(1L);
        Mockito.when(reserve.getOrderId()).thenReturn(2L);

        Transaction transaction = new Transaction(reserve);
        assertEquals(user, transaction.getUser());
        assertEquals(new BigDecimal("50.00"), transaction.getTransactionAmount());
        assertEquals(1L, transaction.getServiceId());
        assertEquals(2L, transaction.getOrderId());
        assertEquals(TransactionType.WITHDRAWAL, transaction.getTransactionType());
        assertNotNull(transaction.getTransactionDateTime());
    }

    @Test
    void testToString() {
        Transaction transaction = new Transaction(user, new BigDecimal("100.00"));
        String expected = "Transaction{transactionIdId=0, user=" + user + ", transactionAmount=100.00, serviceId=0, orderId=0, transactionType=DEPOSIT, transactionDateTime=" + transaction.getTransactionDateTime() + "}";
        assertEquals(expected, transaction.toString());
    }
}
