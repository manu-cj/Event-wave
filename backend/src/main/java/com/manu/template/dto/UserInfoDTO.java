package com.manu.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}