package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestParam BigDecimal availableBalance,
                                           @RequestParam(required = false) BigDecimal reservedBalance) {
        if (reservedBalance == null) {
            reservedBalance = BigDecimal.ZERO;
        }
        userService.createUser(availableBalance, reservedBalance);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
