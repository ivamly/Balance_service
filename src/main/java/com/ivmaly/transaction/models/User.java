package com.ivmaly.transaction.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal availableBalance;

    @Column(nullable = false)
    private BigDecimal reservedBalance;

    public User() {
    }

    public User(BigDecimal availableBalance, BigDecimal reservedBalance) {
        validateNonNegative(availableBalance, "Available balance");
        validateNonNegative(reservedBalance, "Reserved balance");
        this.availableBalance = availableBalance;
        this.reservedBalance = reservedBalance;
    }

    public User(BigDecimal availableBalance) {
        this(availableBalance, BigDecimal.ZERO);
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) throws IllegalArgumentException {
        validateNonNegative(availableBalance, "The available balance");
        this.availableBalance = availableBalance;
    }

    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(BigDecimal reservedBalance) throws IllegalArgumentException {
        validateNonNegative(reservedBalance, "The reserved balance");
        this.reservedBalance = reservedBalance;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", availableBalance=" + availableBalance +
                ", reservedBalance=" + reservedBalance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(availableBalance, user.availableBalance) &&
                Objects.equals(reservedBalance, user.reservedBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, availableBalance, reservedBalance);
    }

    private void validateNonNegative(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
    }
}
