package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Balance;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.BalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(BalanceService.class);

    public BalanceService(BalanceRepository balanceRepository, UserService userService) {
        this.balanceRepository = balanceRepository;
        this.userService = userService;
    }

    @Transactional
    public void updateAvailableBalance(Long userId, BigDecimal newAvailableBalance) {
        Balance balance = getBalanceByUserId(userId);
        balance.setAvailableBalance(newAvailableBalance);
        logger.info("Updated available amount for balance ID: {}", balance.getBalanceId());
        balanceRepository.save(balance);
    }

    @Transactional
    public void updateReservedBalance(Long userId, BigDecimal newReservedBalance) {
        Balance balance = getBalanceByUserId(userId);
        balance.setReservedBalance(newReservedBalance);
        logger.info("Updated reserved amount for balance ID: {}", balance.getBalanceId());
        balanceRepository.save(balance);
    }

    public Balance getBalanceByUserId(Long userId) {
        logger.info("Getting balance by user id: {}", userId);
        User user = userService.getUserById(userId);
        return balanceRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Balance not found for user id: " + userId));
    }

    public Balance createBalance(Balance balance) {
        logger.info("Creating balance: {}", balance.getBalanceId());
        return balanceRepository.save(balance);
    }
}
