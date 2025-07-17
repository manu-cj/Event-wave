package com.manu.template.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false)
    private int postalCode;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false)
    private int availablePlaces;

    @Column(nullable = false, length = 50)
    private String pictureUrl;

}
