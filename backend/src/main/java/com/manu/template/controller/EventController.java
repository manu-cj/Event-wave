package com.manu.template.controller;

import com.manu.template.dto.EventDTO;
import com.manu.template.mapper.EventMapper;
import com.manu.template.model.Event;
import com.manu.template.service.EventService;
import com.manu.template.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Tag(name = "event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final FileService fileService;

    @Operation(summary = "Get all events")
    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(String title, Pageable pageable) {
        Page<EventDTO> response = eventService.findAll(title, pageable);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create new event")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createEvent(
            @RequestPart("event") EventDTO eventDTO,
            @RequestPart("file") MultipartFile file) {

        String fileUrl;

        // Verify picture
        try {
            fileUrl = fileService.storeImage(file);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
        Map<String, String> response = new HashMap<>();
        eventDTO.setPictureUrl(fileUrl != null ? fileUrl : "/uploads/default.jpg");

        try {
            eventService.save(eventDTO);
            response.put("message", "Event add with success !");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error occurred when add event ! " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get event by id")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> findById(@PathVariable UUID eventId) {
            EventDTO event = eventService.findById(eventId);
            if (event == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(event);
    }

    @Operation(summary = "Get 3 last events")
    @GetMapping("/lastEvent")
    public ResponseEntity<List<EventDTO>> findTop3Events() {
        List<EventDTO> response = eventService.findTop3Event();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

}
