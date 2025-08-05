package com.manu.template.controller;

import com.manu.template.dto.ReservationDTO;
import com.manu.template.model.User;
import com.manu.template.service.ReservationService;
import com.manu.template.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final TicketService ticketService;


    @Operation(summary = "get all reservation")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> findAll(String param, Pageable pageable) {
        Page<ReservationDTO> response = reservationService.findAll(param, pageable);
        if (response.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new reservation")
    @PostMapping
    public ResponseEntity<Map<String, String>> createReservation(@RequestBody @Valid ReservationDTO dto) {
        Map<String, String> response = new HashMap<>();
        if (dto.getUser() == null) {
            throw new IllegalArgumentException("L'utilisateur de la réservation ne doit pas être null.");
        }
        try {
            UUID reservationId = UUID.randomUUID();
            String filename = ticketService.generateTicketPdf(reservationId, dto.getUser().getUsername(), dto.getEvent().getTitle());
            dto.setId(reservationId);
            dto.setTicketUrl(filename);
            reservationService.save(dto);
            response.put("message", "Reservation successful");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // TODO Enlever e.getMessage en production
            response.put("message", "Erreur occurred when create a reservation " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @Operation(summary = "Get reservation by user id")
    @GetMapping("/user")
    public ResponseEntity<Page<ReservationDTO>> findByUser(@AuthenticationPrincipal User user, Pageable pageable) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }

        Page<ReservationDTO> response = reservationService.findByUserId(user.getId(), pageable);
        if (response.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{reservationId}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable UUID reservationId) {
        // Récupère la réservation et les infos nécessaires
        ReservationDTO reservation = reservationService.findById(reservationId);
        byte[] pdf = ticketService.getTicketPdfBytes(reservation.getTicketUrl());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=ticket.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}
