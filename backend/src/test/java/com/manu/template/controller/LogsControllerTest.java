package com.manu.template.controller;

import com.manu.template.dto.LogsDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.ActionType;
import com.manu.template.model.Logs;
import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import com.manu.template.security.JwtUtil;
import com.manu.template.service.LogsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LogsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LogsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LogsService logsService;


    // Test configuration to inject mocks into the test context.
    // The JwtUtil mock is mainly used to bypass JWT authentication logic during tests.
    // The UserRepository mock is also required because JwtUtil depends on the repository to load users.
    @TestConfiguration
    static class MockConfig {
        // Mock LogsService to isolate controller tests from actual business logic
        @Bean
        LogsService logsService() {
            return mock(LogsService.class);
        }

        // Mock JwtUtil to avoid real JWT validation in tests
        @Bean
        JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }

        // Mock UserRepository, used by JwtUtil to load users when validating tokens
        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }
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
                .createdAt(logs.getCreatedAt())
                .build();
    }

    @Test
    void should_returnAllLogs() throws Exception {
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");
        Logs logs2 = buildLogs(ActionType.Create, user, "Update a new event");

        List<Logs> logsList = new ArrayList<>(List.of(
                logs,
                logs2
        ));

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<LogsDTO> expectedDTOs = logsList.stream()
                .map(l -> buildLogsDTO(l, userInfoDTO))
                .toList();

        when(logsService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedDTOs, PageRequest.of(0, 10), expectedDTOs.size()));


        mockMvc.perform(get("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].description").value("Create a new event"))
                .andExpect(jsonPath("$.content[1].description").value("Update a new event"));
    }

    @Test
    void should_returnNoContent_whenNoLogsFound() throws Exception {
        when(logsService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_returnLogsById() throws Exception {
        UUID logsId = UUID.randomUUID();
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");
        logs.setId(logsId);

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        LogsDTO expectedLogs = buildLogsDTO(logs, userInfoDTO);

        when(logsService.findById(logsId)).thenReturn(expectedLogs);

        mockMvc.perform(get("/api/logs/{logsId}", logsId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Create a new event"));
    }

    @Test
    void should_returnNoContent_whenNoLogsIsNull() throws Exception {
        UUID logsId = UUID.randomUUID();
        when(logsService.findById(logsId))
                .thenReturn(null);

        mockMvc.perform(get("/api/logs/{logsId}", logsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_returnAllLogsByActionType() throws Exception {
        User user = buildUser();
        Logs logs = buildLogs(ActionType.Create, user, "Create a new event");
        Logs logs2 = buildLogs(ActionType.Update, user, "Update a new event");

        List<Logs> logsList = new ArrayList<>(List.of(
                logs,
                logs2
        ));

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<LogsDTO> expectedDTOs = logsList.stream()
                .filter(l -> l.getActionType() == ActionType.Create)
                .map(l -> buildLogsDTO(l, userInfoDTO))
                .toList();

        when(logsService.findByActionType(eq("Create"),any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedDTOs, PageRequest.of(0, 10), expectedDTOs.size()));


        mockMvc.perform(get("/api/logs/action-type/{actionType}", "Create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].actionType").value("Create"));
    }

    @Test
    void should_returnNoContent_whenNoLogsByActionTypeFound() throws Exception {
        when(logsService.findByActionType(eq("Create"),any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/api/logs/action-type/{actionType}", "Create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
