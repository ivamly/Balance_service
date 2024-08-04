package com.ivmaly.transaction.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull(message = "Transaction amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Transaction amount must be zero or positive")
    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @NotNull(message = "Service ID is mandatory")
    @Column(nullable = false)
    private Long serviceId;

    @NotNull(message = "Order ID is mandatory")
    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is mandatory")
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDateTime;

    public Transaction() {
    }

    public Transaction(User user, BigDecimal transactionAmount, Long serviceId, Long orderId, TransactionType transactionType) {
        this.user = user;
        this.transactionAmount = transactionAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.transactionType = transactionType;
        this.transactionDateTime = LocalDateTime.now();
    }

    public Transaction(User user, BigDecimal transactionAmount, Long serviceId,
                       Long orderId, TransactionType transactionType, LocalDateTime transactionDateTime) {
        this.user = user;
        this.transactionAmount = transactionAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.transactionType = transactionType;
        this.transactionDateTime = transactionDateTime;
    }

    public static Transaction createDepositTransaction(User user, BigDecimal transactionAmount) {
        return new Transaction(user, transactionAmount, 1L, 1L, TransactionType.DEPOSIT, LocalDateTime.now());
    }

    public static Transaction createWithdrawalTransaction(User user, BigDecimal transactionAmount) {
        return new Transaction(user, transactionAmount, -1L, -1L, TransactionType.WITHDRAWAL, LocalDateTime.now());
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", user=" + user +
                ", transactionAmount=" + transactionAmount +
                ", serviceId=" + serviceId +
                ", orderId=" + orderId +
                ", transactionType=" + transactionType +
                ", transactionDateTime=" + transactionDateTime +
                '}';
    }
}
