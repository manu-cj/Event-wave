package com.manu.template.service;

import com.manu.template.dto.ReservationDTO;
import com.manu.template.mapper.ReservationMapper;
import com.manu.template.model.Event;
import com.manu.template.model.Reservation;
import com.manu.template.repository.EventRepository;
import com.manu.template.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;

    public ReservationDTO save(ReservationDTO dto) {
        try {
            log.error(dto.getTicketUrl());
            UUID eventId = dto.getEvent().getId();
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event not found"));

            Reservation reservation = ReservationMapper.toEntity(dto);
            reservation.setEvent(event);

            Reservation saved = reservationRepository.save(reservation);
            return ReservationMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde de la réservation", e);
            throw new RuntimeException("Error occurred when we save reservation in repository", e);
        }
    }

    @Transactional
    public Page<ReservationDTO> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(ReservationMapper::toDto);
    }

    @Transactional
    public Page<ReservationDTO> findByUserId(UUID userId, Pageable pageable) {
        return reservationRepository.findByUserId(userId, pageable)
                .map(ReservationMapper::toDto);
    }

    public ReservationDTO findById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        return ReservationMapper.toDto(reservation);
    }



}
