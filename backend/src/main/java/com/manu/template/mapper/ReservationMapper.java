package com.manu.template.mapper;

import com.manu.template.dto.ReservationDTO;
import com.manu.template.model.Reservation;

public class ReservationMapper {
    public static ReservationDTO toDto(Reservation reservation) {
        if (reservation == null) return null;
        return ReservationDTO.builder()
                .id(reservation.getId())
                .firstname(reservation.getFirstname())
                .lastname(reservation.getLastname())
                .user(UserMapper.toDto(reservation.getUser()))
                .event(EventMapper.toDto(reservation.getEvent()))
                .reservationDate(reservation.getReservationDate())
                .ticketUrl(reservation.getTicketUrl())
                .build();
    }

    public static Reservation toEntity(ReservationDTO reservationDTO) {
        if (reservationDTO == null) return null;
        return Reservation.builder()
                .id(reservationDTO.getId())
                .firstname(reservationDTO.getFirstname())
                .lastname(reservationDTO.getLastname())
                .user(UserMapper.toEntity(reservationDTO.getUser()))
                .event(EventMapper.toEntity(reservationDTO.getEvent()))
                .reservationDate(reservationDTO.getReservationDate())
                .ticketUrl(reservationDTO.getTicketUrl())
                .build();
    }
}
