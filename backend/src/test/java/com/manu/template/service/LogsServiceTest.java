package com.manu.template.service;

import com.manu.template.dto.LogsDTO;
import com.manu.template.repository.LogsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogsServiceTest {
    private LogsRepository logsRepository;
    private LogsService logsService;

    @BeforeEach
    void setup() {
        logsRepository = mock(LogsRepository.class);
        logsService = new LogsService(logsRepository);
    }

    void should_createLogs() {

    }
}
