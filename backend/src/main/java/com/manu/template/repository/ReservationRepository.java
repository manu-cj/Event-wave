package com.manu.template.repository;

import com.manu.template.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Page<Reservation> findByUserId(UUID userId, Pageable pageable);

}