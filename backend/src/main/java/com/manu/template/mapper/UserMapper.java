package com.manu.template.mapper;

import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.User;

public class UserMapper {
    public static UserInfoDTO toDto(User user) {
        return UserInfoDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(String.join(",", user.getRoles()))
                .build();
    }
}
