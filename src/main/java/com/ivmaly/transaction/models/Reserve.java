package com.ivmaly.transaction.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reservation")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reserveId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal reserveAmount;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private ReserveStatus reserveStatus;

    @Column(nullable = false)
    private LocalDateTime reserveDateTime;

    public Reserve() {
    }

    public Reserve(User user, BigDecimal reserveAmount, Long serviceId, Long orderId) {
        this.user = user;
        this.reserveAmount = reserveAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.reserveStatus = ReserveStatus.IN_PROGRESS;
        this.reserveDateTime = LocalDateTime.now();
    }

    public long getReserveId() {
        return reserveId;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getReserveAmount() {
        return reserveAmount;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ReserveStatus getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(ReserveStatus reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public LocalDateTime getReserveDateTime() {
        return reserveDateTime;
    }

    @Override
    public String toString() {
        return "Reserve{" +
                "reserveId=" + reserveId +
                ", user=" + user +
                ", reserveAmount=" + reserveAmount +
                ", serviceId=" + serviceId +
                ", orderId=" + orderId +
                ", reserveStatus=" + reserveStatus +
                ", reserveDateTime=" + reserveDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve reserve = (Reserve) o;
        return reserveId == reserve.reserveId && Objects.equals(user, reserve.user) && Objects.equals(reserveAmount, reserve.reserveAmount) && Objects.equals(serviceId, reserve.serviceId) && Objects.equals(orderId, reserve.orderId) && reserveStatus == reserve.reserveStatus && Objects.equals(reserveDateTime, reserve.reserveDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserveId, user, reserveAmount, serviceId, orderId, reserveStatus, reserveDateTime);
    }
}
