package com.ivmaly.transaction.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionIdId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDateTime;

    public Transaction() {
    }

    public Transaction(User user, BigDecimal transactionAmount, Long serviceId,
                       Long orderId, TransactionType transactionType) {
        this.user = user;
        this.transactionAmount = transactionAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.transactionType = transactionType;
        this.transactionDateTime = LocalDateTime.now();
    }

    public static Transaction createDepositTransaction(User user, BigDecimal transactionAmount) {
        return new Transaction(user, transactionAmount, 0L,
                0L, TransactionType.DEPOSIT);
    }

    public static Transaction createWithdrawalTransaction(User user, BigDecimal transactionAmount) {
        return new Transaction(user, transactionAmount, -1L,
                -1L, TransactionType.WITHDRAWAL);
    }

    public long getTransactionId() {
        return transactionIdId;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionIdId=" + transactionIdId +
                ", user=" + user +
                ", transactionAmount=" + transactionAmount +
                ", serviceId=" + serviceId +
                ", orderId=" + orderId +
                ", transactionType=" + transactionType +
                ", transactionDateTime=" + transactionDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionIdId == that.transactionIdId && Objects.equals(user, that.user) && Objects.equals(transactionAmount, that.transactionAmount) && Objects.equals(serviceId, that.serviceId) && Objects.equals(orderId, that.orderId) && transactionType == that.transactionType && Objects.equals(transactionDateTime, that.transactionDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionIdId, user, transactionAmount, serviceId, orderId, transactionType, transactionDateTime);
    }
}