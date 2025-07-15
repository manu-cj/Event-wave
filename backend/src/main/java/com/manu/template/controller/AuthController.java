package com.manu.template.controller;

import com.manu.template.dto.UserRegistrationDTO;
import com.manu.template.dto.UserLoginDTO;
import com.manu.template.dto.JwtResponseDTO;
import com.manu.template.model.User;
import com.manu.template.security.AuthService;
import com.manu.template.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Utilisateur non authentifié");
        }
        return ResponseEntity.ok(new UserInfo(user.getUsername()));
    }

    public static class UserInfo {
        public String username;

        public UserInfo(String username) {
            this.username = username;
        }
    }

}