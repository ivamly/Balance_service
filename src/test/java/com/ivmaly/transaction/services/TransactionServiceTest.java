package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
    }

    @Test
    void testCreateTransactionSuccess() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "testService";
        String order = "testOrder";
        transactionService.createTransaction(user, amount, service, order);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(user, savedTransaction.getUser());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(service, savedTransaction.getService());
        assertEquals(order, savedTransaction.getOrderDescription());
    }

    @Test
    void testCreateTransactionUserNull() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "testService";
        String order = "testOrder";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(null, amount, service, order));
        assertEquals("User must not be null", exception.getMessage());
    }

    @Test
    void testCreateTransactionAmountNull() {
        String service = "testService";
        String order = "testOrder";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(user, null, service, order));
        assertEquals("Amount must not be null", exception.getMessage());
    }

    @Test
    void testCreateTransactionAmountNegative() {
        BigDecimal amount = BigDecimal.valueOf(-100);
        String service = "testService";
        String order = "testOrder";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(user, amount, service, order));
        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateTransactionServiceNull() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String order = "testOrder";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(user, amount, null, order));
        assertEquals("Service must not be null", exception.getMessage());
    }

    @Test
    void testCreateTransactionOrderNull() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String service = "testService";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(user, amount, service, null));
        assertEquals("Order must not be null", exception.getMessage());
    }
}
