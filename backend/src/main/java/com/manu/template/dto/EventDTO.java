package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @jakarta.validation.constraints.Size(max = 50)
    private String title;

    @NotBlank
    @jakarta.validation.constraints.Size(max = 500)
    private String description;

    @NotNull
    private LocalDateTime date;

    @jakarta.validation.constraints.Size(max = 50)
    private String city;

    @jakarta.validation.constraints.Positive
    private int postalCode;

    @jakarta.validation.constraints.Size(max = 100)
    private String address;

    @jakarta.validation.constraints.Positive
    private int availablePlaces;

    @NotBlank
    private String pictureUrl;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String emailAddress;
}
