package com.manu.template.repository;

import com.manu.template.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Page<Reservation> findByUserId(UUID userId, Pageable pageable);

    Optional<String> findTicketUrlById(UUID id);

    @Query("SELECT r FROM Reservation r WHERE LOWER(r.user.lastname) LIKE LOWER(CONCAT(:name, '%')) OR LOWER(r.user.firstname) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Reservation> searchByUserLastnameOrFirstnameStartingWithIgnoreCase(@Param("name") String name, Pageable pageable);
}
