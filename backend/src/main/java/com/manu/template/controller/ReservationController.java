package com.manu.template.controller;

import com.manu.template.dto.ReservationDTO;
import com.manu.template.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @Operation(summary = "get all reservation")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> findAll(Pageable pageable) {
        Page<ReservationDTO> response = reservationService.findAll(pageable);
        if (response.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new reservation")
    @PostMapping
    public ResponseEntity<Map<String, String>> createReservation(@RequestBody @Valid ReservationDTO dto) {
        Map<String, String> response = new HashMap<>();
        try {
            reservationService.save(dto);
            response.put("message", "Reservation successful");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // TODO Enlever e.getMessage en production
            response.put("message", "Erreur occurred when create a reservation" + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get reservation by user id")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<ReservationDTO>> findByUserId(@PathVariable UUID userId, Pageable pageable) {
        Page<ReservationDTO> response = reservationService.findByUserId(userId, pageable);
        if (response.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


}
