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
                .availablePlaces(event.getAvailablePlaces())
                .date(event.getDate())
                .ville(event.getVille())
                .postalCode(event.getPostalCode())
                .street(event.getStreet())
                .build();
    }

    public static Event toEntity(EventDTO dto) {
        if (dto == null) return null;
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .availablePlaces(dto.getAvailablePlaces())
                .date(dto.getDate())
                .ville(dto.getVille())
                .postalCode(dto.getPostalCode())
                .street(dto.getStreet())
                .build();
    }
}
