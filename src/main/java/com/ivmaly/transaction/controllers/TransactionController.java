package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/reserve/{reserveId}")
    public ResponseEntity<Transaction> createTransactionFromReserve(@PathVariable Long reserveId) {
        Transaction transaction = transactionService.createTransactionFromReserve(reserveId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> createDeposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.createDeposit(userId, amount);
        return ResponseEntity.ok(transaction);
    }
}
