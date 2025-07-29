package com.manu.template.mapper;

import com.manu.template.dto.EventDTO;
import com.manu.template.model.Event;

public class EventMapper {
    public static EventDTO toDto(Event event) {
        if (event == null) return null;
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .city(event.getCity())
                .postalCode(event.getPostalCode())
                .address(event.getAddress())
                .availablePlaces(event.getAvailablePlaces())
                .pictureUrl(event.getPictureUrl())
                .phoneNumber(event.getPhoneNumber())
                .emailAddress(event.getEmailAddress())
                .build();
    }

    public static Event toEntity(EventDTO dto) {
        if (dto == null) return null;
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .date(dto.getDate())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .address(dto.getAddress())
                .availablePlaces(dto.getAvailablePlaces())
                .pictureUrl(dto.getPictureUrl())
                .phoneNumber(dto.getPhoneNumber())
                .emailAddress(dto.getEmailAddress())
                .build();
    }
}
