package com.manu.template.controller;

import com.manu.template.dto.MailChangeDTO;
import com.manu.template.dto.PasswordChangeDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.mapper.UserMapper;
import com.manu.template.model.User;
import com.manu.template.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "get all users")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserInfoDTO>> getAllUsers(@RequestParam(required = false) String param, Pageable pageable) {
        Page<UserInfoDTO> response = userService.getAllUsers(param, pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verify if username already exist")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> usernameExists(@PathVariable String username) {
        boolean exists = userService.usernameExist(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Verify if email already exist")
    @GetMapping("/exist/email/{{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExist(email);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Update user info")
    @PutMapping()
    // Method for update user info with token
    public ResponseEntity<Map<String, String>> updateUserInfo(@AuthenticationPrincipal User user, @RequestBody UserInfoDTO userInfoDTO) {
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("message", "User not authenticated");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            userService.updateUserInfo(user.getId(), userInfoDTO);
            response.put("message", "User info update with success");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Erreur occurred when update User info " + e.getMessage() + " " + user.getId());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> updatePassword(@AuthenticationPrincipal User user, @RequestBody PasswordChangeDTO passwordDto) {
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("message", "User not authenticated");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            userService.passwordChange(user.getId(), passwordDto);
            response.put("message", "User password update with success");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Erreur occurred when update User password " + e.getMessage() + " " + user.getId());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @Operation(summary = "Change email")
    @PutMapping("/change-email")
    public ResponseEntity<Map<String, String>> updateEmail(@AuthenticationPrincipal User user, @RequestBody MailChangeDTO mailChangeDTO) {
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("message", "User not authenticated");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            userService.mailChange(user.getId(), mailChangeDTO);
            response.put("message", "User email update with success");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Erreur occurred when update User email " + e.getMessage() + " " + user.getId());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @Operation(summary = "Update user role")
    @PutMapping("role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateRole(@RequestBody String role, @RequestBody UUID userId) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.changeRole(role, userId);
            response.put("message", "Role change with success");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Erreur occurred when change role " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
