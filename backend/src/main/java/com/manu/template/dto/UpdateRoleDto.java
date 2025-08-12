package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateRoleDto {

    @NotNull
    private UUID userId;

    @NotBlank
    private String role;
}
