package com.manu.template.controller;

import com.manu.template.dto.UserInfoDTO;
import com.manu.template.dto.UserRegistrationDTO;
import com.manu.template.dto.UserLoginDTO;
import com.manu.template.dto.JwtResponseDTO;
import com.manu.template.model.User;
import com.manu.template.security.AuthService;
import com.manu.template.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid UserRegistrationDTO user) {
        userService.registerNewUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return authService.authenticate(userLoginDTO);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody @Valid UserRegistrationDTO user) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.registerNewAdmin(user);
            response.put("message", "Registration Admin successful");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // TODO Enlever e.getMessage en production
            response.put("message", "Erreur occurred when registration Admin " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");

        }
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                String.join(",", user.getRoles())
        );
        return ResponseEntity.ok(userInfo);
    }

}