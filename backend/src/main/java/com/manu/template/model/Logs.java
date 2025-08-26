package com.manu.template.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Logs")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logs {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User author;

    @Column(nullable = false, length = 555)
    private String description;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
}
