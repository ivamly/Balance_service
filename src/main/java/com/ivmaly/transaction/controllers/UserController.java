package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/reserve")
    public ResponseEntity<Map<String, Object>> reserve(@PathVariable("userId") Long userId,
                                                       @RequestParam BigDecimal amount) {
        try {
            userService.reserve(userId, amount);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Amount reserved successfully. Reserve id: ");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@PathVariable("userId") Long userId,
                                                       @RequestParam BigDecimal amount) {
        try {
            userService.deposit(userId, amount);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Amount deposited successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@PathVariable("userId") Long userId,
                                                        @RequestParam BigDecimal amount,
                                                        @RequestParam String service,
                                                        @RequestParam String order) {
        try {
            userService.withdraw(userId, amount, service, order);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Amount withdrawn successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/{userId}/balance/available")
    public ResponseEntity<Map<String, Object>> getAvailableBalance(@PathVariable("userId") Long userId) {
        try {
            BigDecimal availableBalance = userService.getAvailableBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("availableBalance", availableBalance);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/{userId}/balance/reserved")
    public ResponseEntity<Map<String, Object>> getReservedBalance(@PathVariable("userId") Long userId) {
        try {
            BigDecimal reservedBalance = userService.getReservedBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("ReservedBalance", reservedBalance);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }
}