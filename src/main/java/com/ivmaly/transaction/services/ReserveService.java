package com.ivmaly.transaction.services;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.models.ReserveStatus;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.repositories.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ReserveService {
    private final ReserveRepository reserveRepository;
    private final UserService userService;

    @Autowired
    public ReserveService(ReserveRepository reserveRepository, UserService userService) {
        this.reserveRepository = reserveRepository;
        this.userService = userService;
    }

    @Transactional
    public Reserve createReserve(Long userId, BigDecimal reserveAmount, Long serviceId, Long orderId) throws IllegalArgumentException {
        validateNonNegative(reserveAmount);

        User user = userService.getUserById(userId);

        if (user.getAvailableBalance().compareTo(reserveAmount) < 0) {
            throw new IllegalArgumentException("Insufficient available balance for reserve");
        }

        user.setAvailableBalance(user.getAvailableBalance().subtract(reserveAmount));
        user.setReservedBalance(user.getReservedBalance().add(reserveAmount));
        userService.updateUser(user);

        Reserve reserve = new Reserve(user, reserveAmount, serviceId, orderId);
        reserveRepository.save(reserve);

        return reserve;
    }

    @Transactional
    public void cancelReserve(Long reserveId) throws IllegalArgumentException {
        Reserve reserve = getReserveById(reserveId);
        validateStatus(reserve);

        User user = reserve.getUser();
        user.setAvailableBalance(user.getAvailableBalance().add(reserve.getReserveAmount()));
        user.setReservedBalance(user.getReservedBalance().subtract(reserve.getReserveAmount()));

        reserve.setReserveStatus(ReserveStatus.CANCELLED);
        reserveRepository.save(reserve);

        userService.updateUser(user);
    }

    @Transactional
    public void completeReserve(Long reserveId) throws IllegalArgumentException {
        Reserve reserve = getReserveById(reserveId);
        validateStatus(reserve);
        User user = reserve.getUser();
        user.setReservedBalance(user.getReservedBalance().subtract(reserve.getReserveAmount()));
        reserve.setReserveStatus(ReserveStatus.COMPLETED);
        reserveRepository.save(reserve);
    }

    public Reserve getReserveById(Long reserveId) {
        return reserveRepository.findById(reserveId)
                .orElseThrow(() -> new IllegalArgumentException("Reserve not found"));
    }

    public void updateReserve(Reserve reserve) {
        reserveRepository.save(reserve);
    }

    private void validateNonNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Reserve amount cannot be negative");
        }
    }

    private void validateStatus(Reserve reserve) {
        if (reserve.getReserveStatus() != ReserveStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot process reserve with status " + reserve.getReserveStatus());
        }
    }
}
