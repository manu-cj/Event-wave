package com.manu.template.service;

import com.manu.template.dto.EventDTO;
import com.manu.template.mapper.EventMapper;
import com.manu.template.model.Event;
import com.manu.template.repository.EventRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public EventDTO save(EventDTO dto) {
        try {
            Event saved = eventRepository.save(EventMapper.toEntity(dto));
            return EventMapper.toDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred when we save event in repository", e);
        }
    }


    public Page<EventDTO> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(EventMapper::toDto);
    }


}
