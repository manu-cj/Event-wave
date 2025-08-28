package com.manu.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class NotificationsDTO {
    private UUID id;

    @NotNull
    private UserInfoDTO author;

    @NotBlank
    @Size(max = 555)
    private String description;

    private boolean read;

    private LocalDateTime createdAt;
}
