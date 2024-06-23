package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateInput_NullUser_ThrowsException() {
        User user = null;
        User counterparty = new User();
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "Service";
        String order = "Order";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(user, counterparty, amount, service, order);
        });
        assertEquals("User must not be null", exception.getMessage());
    }

    @Test
    public void testValidateInput_NullAmount_ThrowsException() {
        User user = new User();
        User counterparty = new User();
        BigDecimal amount = null;
        String service = "Service";
        String order = "Order";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(user, counterparty, amount, service, order);
        });
        assertEquals("Amount must not be null", exception.getMessage());
    }

    @Test
    public void testValidateInput_NegativeAmount_ThrowsException() {
        User user = new User();
        User counterparty = new User();
        BigDecimal amount = BigDecimal.valueOf(-100);
        String service = "Service";
        String order = "Order";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(user, counterparty, amount, service, order);
        });
        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    public void testValidateInput_NullService_ThrowsException() {
        User user = new User();
        User counterparty = new User();
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = null;
        String order = "Order";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(user, counterparty, amount, service, order);
        });
        assertEquals("Service must not be null", exception.getMessage());
    }

    @Test
    public void testValidateInput_NullOrder_ThrowsException() {
        User user = new User();
        User counterparty = new User();
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "Service";
        String order = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(user, counterparty, amount, service, order);
        });
        assertEquals("Order must not be null", exception.getMessage());
    }
}
