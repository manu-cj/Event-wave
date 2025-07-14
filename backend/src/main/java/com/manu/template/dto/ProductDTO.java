package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double price;
}