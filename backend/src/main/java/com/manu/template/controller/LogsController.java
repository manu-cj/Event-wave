package com.manu.template.controller;

import com.manu.template.dto.LogsDTO;
import com.manu.template.service.LogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "logs")
@RequiredArgsConstructor
public class LogsController {
    private final LogsService logsService;

    @Operation(summary = "get all logs")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LogsDTO>> findAll(Pageable pageable) {
        Page<LogsDTO> response = logsService.findAll(pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
