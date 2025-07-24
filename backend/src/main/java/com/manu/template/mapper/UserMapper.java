package com.manu.template.mapper;

import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.User;

import java.util.Arrays;
import java.util.HashSet;

public class UserMapper {
    public static UserInfoDTO toDto(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(String.join(",", user.getRoles()))
                .build();
    }

    public  static User toEntity(UserInfoDTO userInfoDTO) {
        return User.builder()
                .id(userInfoDTO.getId())
                .username(userInfoDTO.getUsername())
                .email(userInfoDTO.getEmail())
                .firstname(userInfoDTO.getFirstname())
                .lastname(userInfoDTO.getLastname())
                .roles(new HashSet<>(Arrays.asList(userInfoDTO.getRole().split(","))))
                .build();
    }
}
