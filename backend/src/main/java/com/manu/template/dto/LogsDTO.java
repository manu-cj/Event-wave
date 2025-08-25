package com.manu.template.dto;

import com.manu.template.model.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LogsDTO {

    private UUID id;

    private UserInfoDTO author;

    @NotBlank
    @Size(max = 555)
    private String description;

    private ActionType actionType;

    private LocalDateTime createdAt;






}
