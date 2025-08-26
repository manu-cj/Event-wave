package com.manu.template.service;

import com.manu.template.dto.LogsDTO;
import com.manu.template.mapper.LogsMapper;
import com.manu.template.model.Logs;
import com.manu.template.model.User;
import com.manu.template.repository.LogsRepository;
import com.manu.template.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;
    private final UserRepository userRepository;


    public LogsDTO save(Logs logs) {
        try {
            userRepository.findById(logs.getAuthor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Logs saved = logsRepository.save(logs);

            return LogsMapper.toDto(saved);


        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred when we create a new log");
        }
    }

    public LogsDTO findById(UUID id) {
        Logs logs =   logsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Logs not found"));

        return LogsMapper.toDto(logs);
    }
}
