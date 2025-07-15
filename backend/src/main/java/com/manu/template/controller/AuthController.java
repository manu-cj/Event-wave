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

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationDTO user) {
        userService.registerNewUser(user);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return authService.authenticate(userLoginDTO);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid UserRegistrationDTO user) {
        userService.registerNewAdmin(user);
        return ResponseEntity.ok("Registration successful");
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");

        }
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                String.join(",", user.getRoles())
        );
        return ResponseEntity.ok(userInfo);
    }

}