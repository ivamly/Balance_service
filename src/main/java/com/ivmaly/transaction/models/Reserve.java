package com.ivmaly.transaction.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull(message = "Reserve amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Reserve amount must be zero or positive")
    @Column(nullable = false)
    private BigDecimal reserveAmount;

    @NotNull(message = "Service ID is mandatory")
    @Column(nullable = false)
    private Long serviceId;

    @NotNull(message = "Order ID is mandatory")
    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reserve status is mandatory")
    @Column(nullable = false)
    private ReserveStatus reserveStatus;

    @Column(nullable = false)
    private LocalDateTime reserveDateTime;

    public Reserve() {
        this.reserveStatus = ReserveStatus.IN_PROGRESS;
        this.reserveDateTime = LocalDateTime.now();
    }

    public Reserve(User user, BigDecimal reserveAmount, Long serviceId, Long orderId) {
        this.user = user;
        this.reserveAmount = reserveAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.reserveStatus = ReserveStatus.IN_PROGRESS;
        this.reserveDateTime = LocalDateTime.now();
    }

    public Reserve(User user, BigDecimal reserveAmount, Long serviceId, Long orderId, ReserveStatus reserveStatus, LocalDateTime reserveDateTime) {
        this.user = user;
        this.reserveAmount = reserveAmount;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.reserveStatus = reserveStatus;
        this.reserveDateTime = reserveDateTime;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getReserveAmount() {
        return reserveAmount;
    }

    public void setReserveAmount(BigDecimal reserveAmount) {
        this.reserveAmount = reserveAmount;
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

    public ReserveStatus getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(ReserveStatus reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public LocalDateTime getReserveDateTime() {
        return reserveDateTime;
    }

    public void setReserveDateTime(LocalDateTime reserveDateTime) {
        this.reserveDateTime = reserveDateTime;
    }

    @Override
    public String toString() {
        return "Reserve{" +
                "reservationId=" + reservationId +
                ", user=" + user +
                ", reserveAmount=" + reserveAmount +
                ", serviceId=" + serviceId +
                ", orderId=" + orderId +
                ", reserveStatus=" + reserveStatus +
                ", reserveDateTime=" + reserveDateTime +
                '}';
    }
}
