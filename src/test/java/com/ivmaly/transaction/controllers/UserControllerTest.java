package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testReserveSuccess() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        ResponseEntity<Map<String, Object>> response = userController.reserve(userId, amount);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Amount reserved successfully. Reserve id: ", Objects.requireNonNull(response.getBody()).get("message"));
        verify(userService).reserve(userId, amount);
    }

    @Test
    void testReserveBadRequest() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        doThrow(new IllegalArgumentException("Invalid amount")).when(userService).reserve(userId, amount);
        ResponseEntity<Map<String, Object>> response = userController.reserve(userId, amount);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Invalid amount", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testReserveInternalServerError() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        doThrow(new RuntimeException()).when(userService).reserve(userId, amount);
        ResponseEntity<Map<String, Object>> response = userController.reserve(userId, amount);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Internal server error", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testDepositSuccess() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        ResponseEntity<Map<String, Object>> response = userController.deposit(userId, amount);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Amount deposited successfully", Objects.requireNonNull(response.getBody()).get("message"));
        verify(userService).deposit(userId, amount);
    }

    @Test
    void testDepositBadRequest() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        doThrow(new IllegalArgumentException("Invalid amount")).when(userService).deposit(userId, amount);
        ResponseEntity<Map<String, Object>> response = userController.deposit(userId, amount);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Invalid amount", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testDepositInternalServerError() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        doThrow(new RuntimeException()).when(userService).deposit(userId, amount);
        ResponseEntity<Map<String, Object>> response = userController.deposit(userId, amount);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Internal server error", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testWithdrawSuccess() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        String service = "testService";
        String order = "testOrder";
        ResponseEntity<Map<String, Object>> response = userController.withdraw(userId, amount, service, order);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Amount withdrawn successfully", Objects.requireNonNull(response.getBody()).get("message"));
        verify(userService).withdraw(userId, amount, service, order);
    }

    @Test
    void testWithdrawBadRequest() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        String service = "testService";
        String order = "testOrder";
        doThrow(new IllegalArgumentException("Invalid parameters")).when(userService).withdraw(userId, amount, service, order);
        ResponseEntity<Map<String, Object>> response = userController.withdraw(userId, amount, service, order);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Invalid parameters", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testWithdrawInternalServerError() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100");
        String service = "testService";
        String order = "testOrder";
        doThrow(new RuntimeException()).when(userService).withdraw(userId, amount, service, order);
        ResponseEntity<Map<String, Object>> response = userController.withdraw(userId, amount, service, order);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Internal server error", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testGetAvailableBalanceSuccess() {
        Long userId = 1L;
        BigDecimal availableBalance = new BigDecimal("1000");
        when(userService.getAvailableBalance(userId)).thenReturn(availableBalance);
        ResponseEntity<Map<String, Object>> response = userController.getAvailableBalance(userId);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(availableBalance, Objects.requireNonNull(response.getBody()).get("availableBalance"));
        verify(userService).getAvailableBalance(userId);
    }

    @Test
    void testGetAvailableBalanceBadRequest() {
        Long userId = 1L;
        doThrow(new IllegalArgumentException("Invalid user ID")).when(userService).getAvailableBalance(userId);
        ResponseEntity<Map<String, Object>> response = userController.getAvailableBalance(userId);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Invalid user ID", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testGetAvailableBalanceInternalServerError() {
        Long userId = 1L;
        doThrow(new RuntimeException()).when(userService).getAvailableBalance(userId);
        ResponseEntity<Map<String, Object>> response = userController.getAvailableBalance(userId);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Internal server error", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testGetReservedBalanceSuccess() {
        Long userId = 1L;
        BigDecimal reservedBalance = new BigDecimal("500");
        when(userService.getReservedBalance(userId)).thenReturn(reservedBalance);
        ResponseEntity<Map<String, Object>> response = userController.getReservedBalance(userId);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(reservedBalance, Objects.requireNonNull(response.getBody()).get("ReservedBalance"));
        verify(userService).getReservedBalance(userId);
    }

    @Test
    void testGetReservedBalanceBadRequest() {
        Long userId = 1L;
        doThrow(new IllegalArgumentException("Invalid user ID")).when(userService).getReservedBalance(userId);
        ResponseEntity<Map<String, Object>> response = userController.getReservedBalance(userId);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Invalid user ID", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testGetReservedBalanceInternalServerError() {
        Long userId = 1L;
        doThrow(new RuntimeException()).when(userService).getReservedBalance(userId);
        ResponseEntity<Map<String, Object>> response = userController.getReservedBalance(userId);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Internal server error", Objects.requireNonNull(response.getBody()).get("error"));
    }
}
