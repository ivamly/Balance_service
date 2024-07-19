package com.ivmaly.transaction.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReserveTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = Mockito.mock(User.class);
    }

    @Test
    void testReserveConstructor() {
        BigDecimal reserveAmount = new BigDecimal("200.00");
        Long serviceId = 1L;
        Long orderId = 2L;
        Reserve reserve = new Reserve(user, reserveAmount, serviceId, orderId);

        assertEquals(user, reserve.getUser());
        assertEquals(reserveAmount, reserve.getReserveAmount());
        assertEquals(serviceId, reserve.getServiceId());
        assertEquals(orderId, reserve.getOrderId());
        assertEquals(ReserveStatus.IN_PROGRESS, reserve.getReserveStatus());
        assertNotNull(reserve.getReserveDateTime());
    }

    @Test
    void testSetReserveStatus() {
        Reserve reserve = new Reserve(user, new BigDecimal("100.00"), 1L, 2L);
        reserve.setReserveStatus(ReserveStatus.COMPLETED);

        assertEquals(ReserveStatus.COMPLETED, reserve.getReserveStatus());
    }

    @Test
    void testToString() {
        Reserve reserve = new Reserve(user, new BigDecimal("100.00"), 1L, 2L);
        String expected = "Reserve{reserveId=0, user=" + user + ", reserveAmount=100.00, serviceId=1, orderId=2, reserveStatus=IN_PROGRESS, reserveDateTime=" + reserve.getReserveDateTime() + "}";
        assertEquals(expected, reserve.toString());
    }
}
