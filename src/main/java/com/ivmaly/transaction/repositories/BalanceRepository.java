package com.ivmaly.transaction.repositories;

import com.ivmaly.transaction.models.Balance;
import com.ivmaly.transaction.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUser(User user);
}
