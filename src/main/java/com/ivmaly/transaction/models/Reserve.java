package com.ivmaly.transaction.models;

import jakarta.persistence.*;

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

    @Column(nullable = false)
    private BigDecimal reserveAmount;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
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

    public Reserve(User user, BigDecimal reserveAmount, Long serviceId, Long orderId, ReserveStatus reserveStatus,
                   LocalDateTime reserveDateTime) {
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
}
