package com.manu.template.service;

import com.manu.template.dto.LogsDTO;
import com.manu.template.dto.NotificationsDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.Notifications;
import com.manu.template.model.User;
import com.manu.template.repository.NotificationsRepository;
import com.manu.template.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class NotificationsServiceTest {
    private NotificationsRepository notificationsRepository;
    private UserRepository userRepository;
    private NotificationsService notificationsService;

    @BeforeEach
    void setup() {
        notificationsRepository = mock(NotificationsRepository.class);
        userRepository = mock(UserRepository.class);
        notificationsService = new NotificationsService(notificationsRepository, userRepository);
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
}
