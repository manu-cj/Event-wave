package com.manu.template.service;

import com.manu.template.dto.LogsDTO;
import com.manu.template.mapper.LogsMapper;
import com.manu.template.model.Logs;
import com.manu.template.model.User;
import com.manu.template.repository.LogsRepository;
import com.manu.template.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;
    private final UserRepository userRepository;


    public LogsDTO save(LogsDTO logsDto) {
        try {
            Logs logs = LogsMapper.toEntity(logsDto);
            User user = userRepository.findById(logs.getAuthor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            logs.setAuthor(user);

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

    public Page<LogsDTO> findAll(Pageable pageable) {
        return logsRepository.findAll(pageable)
                .map(LogsMapper::toDto);
    }

    public Page<LogsDTO> findByActionType(String actionType,Pageable pageable) {
        return logsRepository.findByActionTypeIgnoreCaseContaining(actionType,pageable)
                .map(LogsMapper::toDto);
    }




}
