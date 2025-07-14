package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}