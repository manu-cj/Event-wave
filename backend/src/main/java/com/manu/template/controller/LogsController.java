package com.manu.template.controller;

import com.manu.template.dto.LogsDTO;
import com.manu.template.model.ActionType;
import com.manu.template.service.LogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(summary = "Get logs by id")
    @GetMapping("/{logsId}")
    public ResponseEntity<LogsDTO> findById(@PathVariable UUID logsId) {
        LogsDTO logs = logsService.findById(logsId);
        if (logs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(logs);

    }

    @Operation(summary = "get all logs by action type")
    @GetMapping("/action-type/{actionType}")
    public ResponseEntity<Page<LogsDTO>> findAllByActionType(@PathVariable String actionType, Pageable pageable) {
        Page<LogsDTO> response = logsService.findByActionType(actionType, pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
