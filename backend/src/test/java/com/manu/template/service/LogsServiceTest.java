package com.manu.template.service;

import com.manu.template.dto.LogsDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.ActionType;
import com.manu.template.model.Logs;
import com.manu.template.model.User;
import com.manu.template.repository.LogsRepository;
import com.manu.template.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.meta.When;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogsServiceTest {
    private LogsRepository logsRepository;
    private UserRepository userRepository;
    private LogsService logsService;

    @BeforeEach
    void setup() {
        logsRepository = mock(LogsRepository.class);
        userRepository = mock(UserRepository.class);
        logsService = new LogsService(logsRepository, userRepository);
    }

    private User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("manu")
                .firstname("Manu")
                .lastname("cj")
                .email("manu@gmail.com")
                .roles(Collections.singleton("USER"))
                .build();
    }

    private UserInfoDTO buildUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(String.join(",", user.getRoles()))
                .build();
    }

    private Logs buildLogs(ActionType actionType, User user, String description) {
        return Logs.builder()
                .id(UUID.randomUUID())
                .author(user)
                .description(description)
                .actionType(actionType)
                .build();
    }

    private LogsDTO buildLogsDTO(Logs logs, UserInfoDTO userInfoDTO) {
        return LogsDTO.builder()
                .id(logs.getId())
                .author(userInfoDTO)
                .description(logs.getDescription())
                .actionType(logs.getActionType())
                .build();
    }

    @Test
    void should_createLogs() {
        // Arrange
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        LogsDTO logsDTO = buildLogsDTO(logs, userInfoDTO);

        when(userRepository.findById(logs.getAuthor().getId())).thenReturn(Optional.of(user));
        when(logsRepository.save(any())).thenReturn(logs);

        // Act
        LogsDTO result = logsService.save(logs);

        // Assert
        verify(logsRepository).save(any(Logs.class));
        assertThat(result).usingRecursiveComparison().isEqualTo(logsDTO);
    }

    @Test
    void should_throwException_whenUserNotFound() {
        // Arrange
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> logsService.save(logs));
        assertTrue(exception.getMessage() == "User not found");
    }

    @Test
    void should_throwRuntimeException_whenUnexpectedErrorOccurs() {
        // Arrange
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(logsRepository.save(any())).thenThrow(new IllegalStateException("DB error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> logsService.save(logs));
        assertEquals("Error occurred when we create a new log", exception.getMessage());
    }

    @Test
    void should_findById() {
        // Arrange
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");
        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        LogsDTO logsDTO = buildLogsDTO(logs, userInfoDTO);
        when(logsRepository.findById(logs.getId())).thenReturn(Optional.of(logs));

        // Act
        LogsDTO result = logsService.findById(logs.getId());

        // Assert
        verify(logsRepository).findById(logs.getId());
        assertThat(result).usingRecursiveComparison().isEqualTo(logsDTO);
    }

    @Test
    void should_throwException_whenLogsNotFound() {
        // Arrange
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");

        when(logsRepository.findById(logs.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> logsService.findById(logs.getId()));
        assertTrue(exception.getMessage() == "Logs not found");
    }

    @Test
    void should_findAll() {
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");
        Logs logs2 = buildLogs(ActionType.Create, user, "Create a new event");

        List<Logs> logsList = new ArrayList<>(List.of(
                logs,
                logs2
        ));

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<LogsDTO> expectedDTOs = logsList.stream()
                .map(l -> buildLogsDTO(l, userInfoDTO))
                .toList();

        List<LogsDTO> expectedDTOs2 = logsList.stream()
                .map(l -> buildLogsDTO(l, userInfoDTO))
                .toList();

        when(logsRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(logsList));

        Page<LogsDTO> result = logsService.findAll(PageRequest.of(0, 10));

        verify(logsRepository).findAll(any(Pageable.class));
        assertEquals(2, result.getContent().size());
        assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(expectedDTOs);
        assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(expectedDTOs2);
    }
}
