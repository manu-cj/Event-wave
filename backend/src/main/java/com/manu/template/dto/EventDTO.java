package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
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

    private LocalDateTime date;

    @jakarta.validation.constraints.Size(max = 50)
    private String ville;

    @jakarta.validation.constraints.Positive
    private int postalCode;

    @jakarta.validation.constraints.Size(max = 100)
    private String street;

    @jakarta.validation.constraints.Positive
    private int availablePlaces;
}
