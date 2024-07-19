package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.models.ReserveStatus;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.ReserveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReserveServiceTest {

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReserveService reserveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReserve_withSufficientBalance_createsReserve() {
        Long userId = 1L;
        BigDecimal reserveAmount = new BigDecimal("50.00");
        Long serviceId = 1L;
        Long orderId = 1L;
        User user = new User(new BigDecimal("100.00"));

        when(userService.getUserById(userId)).thenReturn(user);

        reserveService.createReserve(userId, reserveAmount, serviceId, orderId);

        verify(userService, times(1)).updateUser(user);
        verify(reserveRepository, times(1)).save(any(Reserve.class));
    }

    @Test
    void createReserve_withInsufficientBalance_throwsException() {
        Long userId = 1L;
        BigDecimal reserveAmount = new BigDecimal("150.00");
        Long serviceId = 1L;
        Long orderId = 1L;
        User user = new User(new BigDecimal("100.00"));

        when(userService.getUserById(userId)).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reserveService.createReserve(userId, reserveAmount, serviceId, orderId));

        assertEquals("Insufficient available balance for reserve", exception.getMessage());
        verify(userService, never()).updateUser(user);
        verify(reserveRepository, never()).save(any(Reserve.class));
    }

    @Test
    void completeReserve_withInProgressStatus_completesReserve() {
        Long reserveId = 1L;
        Reserve reserve = new Reserve();
        reserve.setReserveStatus(ReserveStatus.IN_PROGRESS);

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.of(reserve));

        reserveService.completeReserve(reserveId);

        assertEquals(ReserveStatus.COMPLETED, reserve.getReserveStatus());
        verify(reserveRepository, times(1)).save(reserve);
    }

    @Test
    void completeReserve_withNonInProgressStatus_throwsException() {
        Long reserveId = 1L;
        Reserve reserve = new Reserve();
        reserve.setReserveStatus(ReserveStatus.CANCELLED);

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.of(reserve));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reserveService.completeReserve(reserveId));

        assertEquals("Cannot process reserve with status CANCELLED", exception.getMessage());
        verify(reserveRepository, never()).save(reserve);
    }

    @Test
    void getReserveById_existingReserve_returnsReserve() {
        Long reserveId = 1L;
        Reserve reserve = new Reserve();

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.of(reserve));

        Reserve foundReserve = reserveService.getReserveById(reserveId);

        assertNotNull(foundReserve);
        assertEquals(reserve, foundReserve);
        verify(reserveRepository, times(1)).findById(reserveId);
    }

    @Test
    void getReserveById_nonExistingReserve_throwsException() {
        Long reserveId = 1L;

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reserveService.getReserveById(reserveId));

        assertEquals("Reserve not found", exception.getMessage());
        verify(reserveRepository, times(1)).findById(reserveId);
    }
}
