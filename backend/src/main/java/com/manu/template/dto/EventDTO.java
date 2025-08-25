package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventDTO {
    private UUID id;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDateTime date;

    @Size(max = 50)
    private String city;

    @Positive
    private int postalCode;

    @Size(max = 100)
    private String address;

    @Positive
    private int availablePlaces;

    @NotBlank
    private String pictureUrl;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String emailAddress;
}
