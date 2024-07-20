package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.services.ReserveService;
import com.ivmaly.transaction.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reserves")
public class ReserveController {

    private final ReserveService reserveService;
    private final UserService userService;

    public ReserveController(ReserveService reserveService, UserService userService) {
        this.reserveService = reserveService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Reserve> createReserve(@RequestParam Long userId, @RequestParam BigDecimal reserveAmount,
                                                 @RequestParam Long serviceId, @RequestParam Long orderId) {
        Reserve reserve = reserveService.createReserve(userId, reserveAmount, serviceId, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserve);
    }

    @PostMapping("/{reserveId}/cancel")
    public ResponseEntity<Void> cancelReserve(@PathVariable Long reserveId) {
        reserveService.cancelReserve(reserveId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reserveId}/complete")
    public ResponseEntity<Void> completeReserve(@PathVariable Long reserveId) {
        reserveService.completeReserve(reserveId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reserveId}")
    public ResponseEntity<Reserve> getReserveById(@PathVariable Long reserveId) {
        Reserve reserve = reserveService.getReserveById(reserveId);
        return ResponseEntity.ok(reserve);
    }
}
