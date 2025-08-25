package com.manu.template.mapper;

import com.manu.template.dto.LogsDTO;
import com.manu.template.model.Logs;
import lombok.extern.java.Log;

public class LogsMapper {

    public static LogsDTO toDto(Logs logs) {
        if (logs == null) return  null;
        return LogsDTO.builder()
                .id(logs.getId())
                .author(UserMapper.toDto(logs.getAuthor()))
                .description(logs.getDescription())
                .actionType(logs.getActionType())
                .createdAt(logs.getCreatedAt())
                .build();
    }

    public static Logs toEntity(LogsDTO logsDTO) {
        if (logsDTO == null) return  null;
        return Logs.builder()
                .id(logsDTO.getId())
                .author(UserMapper.toEntity(logsDTO.getAuthor()))
                .description(logsDTO.getDescription())
                .actionType(logsDTO.getActionType())
                .createdAt(logsDTO.getCreatedAt())
                .build();
    }
}
