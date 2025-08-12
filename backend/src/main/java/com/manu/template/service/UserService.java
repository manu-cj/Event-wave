package com.manu.template.service;

import com.manu.template.dto.*;
import com.manu.template.mapper.UserMapper;
import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

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

    public Page<UserInfoDTO> getAllUsers(String param,Pageable pageable) {
        return userRepository.searchBySingleParam(param, pageable)
                .map(UserMapper::toDto);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user;
    }

    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public User changeRole(UpdateRoleDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setRoles(new HashSet<>(Collections.singleton(dto.getRole())));

        return userRepository.save(user);
    }

    @Transactional
    public UserInfoDTO updateUserInfo(UUID userId, UserInfoDTO userInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setUsername(userInfo.getUsername());
        user.setFirstname(userInfo.getFirstname());
        user.setLastname(userInfo.getLastname());

        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Transactional
    public UserInfoDTO passwordChange(UUID userId, PasswordChangeDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), dto.getNewPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Transactional
    public UserInfoDTO mailChange(UUID userId, MailChangeDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        user.setEmail(dto.getMail());

        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }
}