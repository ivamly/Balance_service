package com.ivmaly.transaction.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull(message = "Available amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Available amount must be zero or positive")
    @Column(nullable = false)
    private BigDecimal availableAmount;

    @NotNull(message = "Reserved amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Reserved amount must be zero or positive")
    @Column(nullable = false)
    private BigDecimal reservedAmount;

    public Balance() {
    }

    public Balance(User user, BigDecimal availableAmount, BigDecimal reservedAmount) {
        this.user = user;
        this.availableAmount = availableAmount;
        this.reservedAmount = reservedAmount;
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balanceId=" + balanceId +
                ", user=" + user +
                ", availableAmount=" + availableAmount +
                ", reservedAmount=" + reservedAmount +
                '}';
    }
}
