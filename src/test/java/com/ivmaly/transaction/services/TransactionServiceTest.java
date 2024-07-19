package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.*;
import com.ivmaly.transaction.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ReserveService reserveService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransactionFromReserve_createsTransactionAndUpdatesUserBalance() {
        Long reserveId = 1L;
        User user = new User(new BigDecimal("100.00"), new BigDecimal("50.00"));
        Reserve reserve = new Reserve(user, new BigDecimal("50.00"), 1L, 1L);
        reserve.setReserveStatus(ReserveStatus.IN_PROGRESS);

        when(reserveService.getReserveById(reserveId)).thenReturn(reserve);

        Transaction transaction = transactionService.createTransactionFromReserve(reserveId);

        assertNotNull(transaction);
        assertEquals(TransactionType.WITHDRAWAL, transaction.getTransactionType());
        assertEquals(user, transaction.getUser());
        assertEquals(new BigDecimal("50.00"), transaction.getTransactionAmount());
        assertEquals(new BigDecimal("0.00"), user.getReservedBalance());

        verify(userService, times(1)).updateUser(user);
        verify(transactionRepository, times(1)).save(transaction);
        verify(reserveService, times(1)).updateReserve(reserve);
    }

    @Test
    void createDeposit_createsTransactionAndUpdatesUserBalance() {
        Long userId = 1L;
        BigDecimal depositAmount = new BigDecimal("50.00");
        User user = new User(new BigDecimal("100.00"), new BigDecimal("0.00"));

        when(userService.getUserById(userId)).thenReturn(user);

        Transaction transaction = transactionService.createDeposit(userId, depositAmount);

        assertNotNull(transaction);
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertEquals(user, transaction.getUser());
        assertEquals(depositAmount, transaction.getTransactionAmount());
        assertEquals(new BigDecimal("150.00"), user.getAvailableBalance());

        verify(userService, times(1)).updateUser(user);
        verify(transactionRepository, times(1)).save(transaction);
    }
}
