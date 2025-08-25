package com.manu.template.dto;

import com.manu.template.model.Event;
import com.manu.template.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class ReservationDTO {
    private UUID id;

    private String firstname;

    private String lastname;

    private UserInfoDTO user;

    private EventDTO event;

    @NotNull
    private LocalDateTime reservationDate;

    private String ticketUrl;
}
