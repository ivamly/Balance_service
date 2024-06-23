package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction_ValidData_SavesTransaction() {
        // Arrange
        User user = new User();
        BigDecimal amount = BigDecimal.TEN;
        String service = "DEPOSIT";
        String order = "NONE";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        transactionService.createTransactionWithouCounterpary(user, amount, service, order);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testCreateTransaction_NullUser_ThrowsException() {
        // Arrange
        BigDecimal amount = BigDecimal.TEN;
        String service = "DEPOSIT";
        String order = "NONE";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransactionWithouCounterpary(null, amount, service, order));
    }

    @Test
    public void testCreateTransaction_NullAmount_ThrowsException() {
        // Arrange
        User user = new User();
        String service = "DEPOSIT";
        String order = "NONE";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransactionWithouCounterpary(user, null, service, order));
    }

    @Test
    public void testCreateTransaction_NegativeAmount_ThrowsException() {
        // Arrange
        User user = new User();
        BigDecimal amount = BigDecimal.valueOf(-10);
        String service = "DEPOSIT";
        String order = "NONE";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransactionWithouCounterpary(user, amount, service, order));
    }
}
