package com.ivmaly.transaction.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @OneToOne
    @JoinColumn
    private User user;

    @Column(nullable = false)
    private BigDecimal availableAmount;

    @Column(nullable = false)
    private BigDecimal reservedAmount;

    public Balance() {
    }

    public Balance(User user, BigDecimal availableAmount, BigDecimal reservedAmount) {
        this.user = user;
        this.availableAmount = availableAmount;
        this.reservedAmount = reservedAmount;
    }

    public Long getId() {
        return balanceId;
    }

    public void setId(Long id) {
        this.balanceId = id;
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
                "id=" + balanceId +
                ", user=" + user +
                ", availableAmount=" + availableAmount +
                ", reservedAmount=" + reservedAmount +
                '}';
    }
}
