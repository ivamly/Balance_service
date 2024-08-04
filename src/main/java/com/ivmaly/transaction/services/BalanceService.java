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
    private static final Logger logger = LoggerFactory.getLogger(BalanceService.class);

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Transactional
    public void updateAvailableBalance(Balance balance, BigDecimal newAvailableBalance) {
        balance.setAvailableBalance(newAvailableBalance);
        logger.info("Updated available amount for balance ID: {}", balance.getBalanceId());
        balanceRepository.save(balance);
    }

    @Transactional
    public void updateReservedBalance(Balance balance, BigDecimal newReservedBalance) {
        balance.setReservedBalance(newReservedBalance);
        logger.info("Updated reserved amount for balance ID: {}", balance.getBalanceId());
        balanceRepository.save(balance);
    }

    public Balance getBalanceByUser(User user) {
        logger.info("Getting balance by user id: {}", user.getUserId());
        return balanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Balance not found for user id: " + user.getUserId()));
    }

    public Balance createBalance(Balance balance) {
        logger.info("Creating balance: {}", balance.getBalanceId());
        return balanceRepository.save(balance);
    }
}
