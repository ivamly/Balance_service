package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.services.ReserveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reserves")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @PostMapping
    public ResponseEntity<?> createReserve(@RequestParam Long userId,
                                              @RequestParam BigDecimal reserveAmount,
                                              @RequestParam Long serviceId,
                                              @RequestParam Long orderId) {
        try {
            reserveService.createReserve(userId, reserveAmount, serviceId, orderId);
            return ResponseEntity.ok("Reserve successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{reserveId}/cancel")
    public ResponseEntity<Void> cancelReserve(@PathVariable Long reserveId) {
        try {
            reserveService.undoReserve(reserveId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{reserveId}/complete")
    public ResponseEntity<Void> completeReserve(@PathVariable Long reserveId) {
        try {
            reserveService.completeReserve(reserveId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{reserveId}")
    public ResponseEntity<Reserve> getReserveById(@PathVariable Long reserveId) {
        try {
            Reserve reserve = reserveService.getReserveById(reserveId);
            return ResponseEntity.ok(reserve);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
