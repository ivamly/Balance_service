package com.ivmaly.transaction.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal reservedBalance = BigDecimal.ZERO;

    public User() {
    }

    public User(BigDecimal balance) {
        this.availableBalance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        if (availableBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The available balance cannot be negative");
        }
        this.availableBalance = availableBalance;
    }

    public BigDecimal getReservedBalance() {
        if (reservedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The reserved balance cannot be negative");
        }
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }
}