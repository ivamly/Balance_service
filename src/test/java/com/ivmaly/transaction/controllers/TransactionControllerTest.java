package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransactionFromReserve() throws Exception {
        Transaction transaction = new Transaction(new User(new BigDecimal("100.00")), new BigDecimal("50.00"));
        when(transactionService.createTransactionFromReserve(any(Long.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions/reserve/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionAmount").value(50.00));
    }

    @Test
    void createDeposit() throws Exception {
        Transaction transaction = new Transaction(new User(new BigDecimal("100.00")), new BigDecimal("50.00"));
        when(transactionService.createDeposit(any(Long.class), any(BigDecimal.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions/deposit")
                        .param("userId", "1")
                        .param("amount", "50.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionAmount").value(50.00));
    }
}
