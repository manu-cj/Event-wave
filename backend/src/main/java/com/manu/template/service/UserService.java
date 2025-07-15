package com.manu.template.service;

import com.manu.template.dto.UserRegistrationDTO;
import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .roles(Collections.singleton("USER"))
                .build();
        return userRepository.save(user);
    }

    public User registerNewAdmin(UserRegistrationDTO registrationDTO) {
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .roles(Collections.singleton("ADMIN"))
                .build();
        return userRepository.save(user);
    }
}