package com.manu.template.service;

import com.manu.template.dto.ReservationDTO;
import com.manu.template.mapper.ReservationMapper;
import com.manu.template.model.Reservation;
import com.manu.template.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationDTO save(ReservationDTO dto) {
        try {
            Reservation saved = reservationRepository.save(ReservationMapper.toEntity(dto));
            return ReservationMapper.toDto(saved);
        } catch (Exception e) {
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



}
