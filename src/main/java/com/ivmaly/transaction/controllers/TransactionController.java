package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Transaction;
import com.ivmaly.transaction.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<?> deposit(@PathVariable("userId") Long userId,
                                     @RequestParam BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero.");
        }
        try {
            Transaction transaction = transactionService.deposit(userId, amount);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(transaction);
        } catch (Exception e) {
            logger.error("Error processing deposit for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing deposit.");
        }
    }

    @PostMapping("/withdrawal/{userId}")
    public ResponseEntity<?> withdrawal(@PathVariable("userId") Long userId,
                                        @RequestParam BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero.");
        }
        try {
            Transaction transaction = transactionService.withdraw(userId, amount);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(transaction);
        } catch (Exception e) {
            logger.error("Error processing withdrawal for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing withdrawal.");
        }
    }

    @PostMapping("/transfer/{userFromId}/{userToId}")
    public ResponseEntity<?> transfer(@PathVariable("userFromId") Long userFromId,
                                      @PathVariable("userToId") Long userToId,
                                      @RequestParam BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than zero.");
        }
        try {
            transactionService.transfer(userFromId, userToId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing transfer from userId {} to userId {}: {}", userFromId, userToId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing transfer.");
        }
    }
}
