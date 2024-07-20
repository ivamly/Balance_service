package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<Transaction> deposit(@PathVariable("userId") Long userId,
                                               @RequestBody BigDecimal amount) {
        if (amount.signum() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Transaction transaction = transactionService.createDeposit(userId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdrawal/{userId}")
    public ResponseEntity<Transaction> withdrawal(@PathVariable("userId") Long userId,
                                                  @RequestBody BigDecimal amount) {
        if (amount.signum() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Transaction transaction = transactionService.createWithdraw(userId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer/{userFromId}/{userToId}")
    public ResponseEntity<List<Transaction>> transfer(@PathVariable("userFromId") Long userFromId,
                                                      @PathVariable("userToId") Long userToId,
                                                      @RequestBody BigDecimal amount) {
        if (amount.signum() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Transaction> transactions = transactionService.createTransfer(userFromId, userToId, amount);
        return ResponseEntity.ok(transactions);
    }
}
