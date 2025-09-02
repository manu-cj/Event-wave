package com.manu.template.controller;

import com.manu.template.dto.NotificationsDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.Notifications;
import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import com.manu.template.security.JwtUtil;
import com.manu.template.service.NotificationsService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NotificationsService notificationsService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        NotificationsService notificationsService() {
            return mock(NotificationsService.class);
        }

        @Bean
        JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }

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

    private Notifications buildNotifications(User user, String description) {
        return Notifications.builder()
                .id(UUID.randomUUID())
                .author(user)
                .description(description)
                .read(false)
                .build();
    }

    private NotificationsDTO buildNotificationsDTO(Notifications notifications, UserInfoDTO userInfoDTO) {
        return NotificationsDTO.builder()
                .id(notifications.getId())
                .author(userInfoDTO)
                .description(notifications.getDescription())
                .read(notifications.isRead())
                .build();
    }

    @Test
    void should_returnAllNotificationByUserId() throws Exception {
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Event start soon");
        Notifications notifications2 = buildNotifications(user, "Your reservation has been reserved");

        List<Notifications> notificationsList = new ArrayList<>(List.of(
                notifications,
                notifications2
        ));

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<NotificationsDTO> expectedDTOs = notificationsList.stream()
                        .map(n -> buildNotificationsDTO(n, userInfoDTO))
                        .toList();

        when(notificationsService.findByAuthorId(eq(user.getId()), any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedDTOs, PageRequest.of(0, 10), expectedDTOs.size()));

        // Simulate authenticated user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null)
        );

        mockMvc.perform(get("/api/notifications")
                        .principal(new UsernamePasswordAuthenticationToken(user, null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].description").value("Event start soon"))
                .andExpect(jsonPath("$.content[1].description").value("Your reservation has been reserved"));
    }

    @Test
    void should_returnNoContent_whenNoNotificationsFound() throws Exception {
        User user = buildUser();
        when(notificationsService.findByAuthorId(eq(user.getId()),any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null)
        );

        mockMvc.perform(get("/api/notifications")
                        .principal(new UsernamePasswordAuthenticationToken(user, null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_returnUnauthorized_whenUserIsNull() throws Exception {
        mockMvc.perform(get("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}
