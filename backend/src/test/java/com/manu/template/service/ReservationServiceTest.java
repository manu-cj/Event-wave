package com.manu.template.service;
import com.manu.template.dto.EventDTO;
import com.manu.template.dto.ReservationDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.mapper.ReservationMapper;
import com.manu.template.model.Event;
import com.manu.template.model.Reservation;
import com.manu.template.model.User;
import com.manu.template.repository.EventRepository;
import com.manu.template.repository.LogsRepository;
import com.manu.template.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private ReservationRepository reservationRepository;
    @InjectMocks private ReservationService reservationService;




    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Event buildEvent() {
        return Event.builder()
                .id(UUID.randomUUID()) // ou passe l’id en paramètre si besoin
                .title("Conférence Angular")
                .description("Une conférence sur Angular")
                .date(LocalDateTime.now())
                .city("Charleroi")
                .postalCode(6000)
                .address("Sq. des Martyrs 1")
                .availablePlaces(10)
                .pictureUrl("pic.jpg")
                .phoneNumber("0491/.11.11.11")
                .emailAddress("manu@gmail.com")
                .build();
    }

    private  User buildUser() {
       return User.builder()
               .id(UUID.randomUUID())
               .username("manu")
               .firstname("Manu")
               .lastname("cj")
               .email("manu@gmail.com")
               .roles(Collections.singleton("USER"))
               .build();
    }

    private UserInfoDTO buildUserInfoDTO(UUID userId) {
        return UserInfoDTO.builder()
                .id(userId)
                .username("manu")
                .firstname("Manu")
                .lastname("cj")
                .email("manu@gmail.com")
                .role("USER")
                .build();
    }

    private EventDTO buildEventDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .city(event.getCity())
                .postalCode(event.getPostalCode())
                .address(event.getAddress())
                .availablePlaces(event.getAvailablePlaces())
                .pictureUrl(event.getPictureUrl())
                .phoneNumber(event.getPhoneNumber())
                .emailAddress(event.getEmailAddress())
                .build();
    }

    private Reservation buildReservation(UUID reservationId, User user, Event event, LocalDateTime now) {
        return Reservation.builder()
                .id(reservationId)
                .firstname("Manu")
                .lastname("Cj")
                .user(user)
                .event(event)
                .reservationDate(now)
                .ticketUrl("ticket.pdf")
                .build();
    }

    private ReservationDTO buildReservationDTO(UUID reservationId, UserInfoDTO userInfoDTO, EventDTO eventDTO, LocalDateTime now) {
        return ReservationDTO.builder()
                .id(reservationId)
                .firstname("Manu")
                .lastname("Cj")
                .user(userInfoDTO)
                .event(eventDTO)
                .reservationDate(now)
                .ticketUrl("ticket.pdf")
                .build();
    }


    @Test
    void save_ShouldSaveReservationAndDecreaseAvailablePlaces() {
        // Arrange
        UUID reservationId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        User user = buildUser();
        Event event = buildEvent();
        event.setId(eventId);
        UserInfoDTO userInfoDTO = buildUserInfoDTO(user.getId());
        EventDTO eventDTO = buildEventDTO(event);
        Reservation reservation = buildReservation(reservationId, user, event, now);
        ReservationDTO dto = buildReservationDTO(reservationId, userInfoDTO, eventDTO, now);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(reservationRepository.save(any())).thenReturn(reservation);

        EventDTO expectedEventDTO = eventDTO.toBuilder().availablePlaces(9).build();

        // Act
        ReservationDTO result = reservationService.save(dto);

        // Assert
        verify(eventRepository).save(event);
        verify(reservationRepository).save(any(Reservation.class));
        assertEquals(dto.getFirstname(), result.getFirstname());
        assertEquals(dto.getLastname(), result.getLastname());
        assertEquals(dto.getUser(), result.getUser());
        assertEquals(expectedEventDTO, result.getEvent());
        assertEquals(9, event.getAvailablePlaces());
        assertEquals(dto.getReservationDate(), result.getReservationDate());
        assertEquals(dto.getTicketUrl(), result.getTicketUrl());
    }
}
