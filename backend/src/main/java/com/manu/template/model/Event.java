package com.manu.template.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "event")
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 255)
    private String city;

    @Column(nullable = false)
    private int postalCode;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private int availablePlaces;

    @Column(nullable = false, length = 255)
    private String pictureUrl;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String emailAddress;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void decreaseAvailablePlaces() {
        if (availablePlaces > 0) {
            availablePlaces--;
        } else {
            throw new IllegalStateException("No place available.");
        }
    }

}
