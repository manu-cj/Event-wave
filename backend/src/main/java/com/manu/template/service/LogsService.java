package com.manu.template.service;

import com.manu.template.repository.LogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;
}
