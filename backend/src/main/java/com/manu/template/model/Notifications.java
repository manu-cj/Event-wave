package com.manu.template.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notifications {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User author;

    private String description;

    private boolean read = false;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public void markAsRead() {
        this.setRead(true);
    }
}
