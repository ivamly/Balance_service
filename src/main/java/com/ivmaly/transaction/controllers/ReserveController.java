package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.services.ReserveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reserves")
public class ReserveController {

    private final ReserveService reserveService;
    private static final Logger logger = LoggerFactory.getLogger(ReserveController.class);

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @PostMapping
    public ResponseEntity<String> createReserve(@RequestParam Long userId,
                                                @RequestParam BigDecimal reserveAmount,
                                                @RequestParam Long serviceId,
                                                @RequestParam Long orderId) {
        if (reserveAmount == null || reserveAmount.signum() <= 0) {
            return ResponseEntity.badRequest().body("Reserve amount must be greater than zero.");
        }
        try {
            reserveService.createReserve(userId, reserveAmount, serviceId, orderId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Reserve successful");
        } catch (IllegalArgumentException e) {
            logger.error("Error creating reserve for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error creating reserve for userId {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating reserve.");
        }
    }

    @PostMapping("/{reserveId}/cancel")
    public ResponseEntity<Void> cancelReserve(@PathVariable Long reserveId) {
        try {
            reserveService.undoReserve(reserveId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error canceling reserve with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error canceling reserve with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{reserveId}/complete")
    public ResponseEntity<Void> completeReserve(@PathVariable Long reserveId) {
        try {
            reserveService.completeReserve(reserveId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error completing reserve with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error completing reserve with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{reserveId}")
    public ResponseEntity<Reserve> getReserveById(@PathVariable Long reserveId) {
        try {
            Reserve reserve = reserveService.getReserveById(reserveId);
            return ResponseEntity.ok(reserve);
        } catch (IllegalArgumentException e) {
            logger.error("Reserve not found with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching reserve with reserveId {}: {}", reserveId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
