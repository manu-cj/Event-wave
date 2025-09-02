package com.manu.template.controller;

import com.manu.template.dto.NotificationsDTO;
import com.manu.template.model.User;
import com.manu.template.service.NotificationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationsService notificationsService;

    @Operation(summary = "Get all notification by user")
    @GetMapping
    public ResponseEntity<Page<NotificationsDTO>> findAllByUserId(@AuthenticationPrincipal User user, Pageable pageable){
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Page<NotificationsDTO> response = notificationsService.findByAuthorId(user.getId(), pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

}
