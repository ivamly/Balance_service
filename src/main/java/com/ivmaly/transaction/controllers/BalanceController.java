package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Balance;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.services.BalanceService;
import com.ivmaly.transaction.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;
    private final UserService userService;

    public BalanceController(BalanceService balanceService, UserService userService) {
        this.balanceService = balanceService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Balance> create(@PathVariable Long userId, @RequestBody Balance balance) {
        try {
            User user = userService.getUserById(userId);
            balance.setUser(user);
            Balance createdBalance = balanceService.createBalance(balance);
            return ResponseEntity
                    .status(HttpStatus.CREATED).
                    body(createdBalance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Balance> getBalanceById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Balance balance = balanceService.getBalanceByUser(user);
        return ResponseEntity.ok(balance);
    }
}
