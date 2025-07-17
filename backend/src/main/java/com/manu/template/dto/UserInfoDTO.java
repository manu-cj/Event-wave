package com.manu.template.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}