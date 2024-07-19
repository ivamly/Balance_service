package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.services.UserService;
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
    public void createUser(@RequestParam BigDecimal availableBalance,
                           @RequestParam(required = false) BigDecimal reservedBalance) {
        if (reservedBalance == null) {
            reservedBalance = BigDecimal.ZERO;
        }
        userService.createUser(availableBalance, reservedBalance);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
