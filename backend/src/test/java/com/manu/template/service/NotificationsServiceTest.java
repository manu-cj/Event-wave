package com.manu.template.service;

import com.manu.template.dto.NotificationsDTO;
import com.manu.template.dto.UserInfoDTO;
import com.manu.template.model.Notifications;
import com.manu.template.model.User;
import com.manu.template.repository.NotificationsRepository;
import com.manu.template.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void should_createNotification() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        NotificationsDTO notificationsDTO = buildNotificationsDTO(notifications, userInfoDTO);

        when(userRepository.findById(notifications.getAuthor().getId())).thenReturn(Optional.of(user));
        when(notificationsRepository.save(any())).thenReturn(notifications);

        // Act
        NotificationsDTO result = notificationsService.save(notificationsDTO);

        // Assert
        verify(notificationsRepository).save(any(Notifications.class));
        assertThat(result).usingRecursiveComparison().isEqualTo(notificationsDTO);
    }

    @Test
    void should_throwException_whenUserNotFound() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        NotificationsDTO notificationsDTO = buildNotificationsDTO(notifications, userInfoDTO);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> notificationsService.save(notificationsDTO));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void should_throwRuntimeException_whenUnexpectedErrorOccurs() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        NotificationsDTO notificationsDTO = buildNotificationsDTO(notifications, userInfoDTO);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(notificationsRepository.save(any())).thenThrow(new IllegalStateException("DB error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> notificationsService.save(notificationsDTO));
        assertEquals("Error occurred when we create a notification", exception.getMessage());
    }


    @Test
    void should_findAllNotificationByUserId() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");
        Notifications notifications2 = buildNotifications(user, "Your reservation has been reserved");

        List<Notifications> notificationsList = new ArrayList<>(List.of(
                notifications,
                notifications2
        ));

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<NotificationsDTO> expectedDTOs = notificationsList.stream()
                .map(n -> buildNotificationsDTO(n, userInfoDTO))
                .toList();

        when(notificationsRepository.findByAuthorId(eq(user.getId()), any(Pageable.class))).thenReturn(new PageImpl<>(notificationsList));

        // Act
        Page<NotificationsDTO> result = notificationsService.findByAuthorId(user.getId(), PageRequest.of(0, 10));

        // Assert
        verify(notificationsRepository).findByAuthorId(eq(user.getId()), any(Pageable.class));
        assertEquals(2, result.getContent().size());
        assertEquals("Your event start soon", expectedDTOs.get(0).getDescription());
        assertEquals("Your reservation has been reserved", expectedDTOs.get(1).getDescription());
    }

    @Test
    void should_updateNotificationInRead() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        NotificationsDTO notificationsDTO = buildNotificationsDTO(notifications, userInfoDTO);

        notifications.markAsRead();
        when(notificationsRepository.findById(notifications.getId())).thenReturn(Optional.of(notifications));
        when(notificationsRepository.save(any())).thenReturn(notifications);

        // Act
        NotificationsDTO result = notificationsService.isRead(notificationsDTO.getId());

        // Assert
        verify(notificationsRepository).save(any(Notifications.class));
        assertTrue(result.isRead());
    }

    @Test
    void should_updateAllNotificationInRead() {
        // Arrange
        User user = buildUser();
        Notifications notifications = buildNotifications(user, "Your event start soon");
        Notifications notifications2 = buildNotifications(user, "Your reservation has been reserved");

        List<Notifications> notificationsList = new ArrayList<>(List.of(
                notifications,
                notifications2
        ));

        notificationsList.forEach(Notifications::markAsRead);

        UserInfoDTO userInfoDTO = buildUserInfoDTO(user);
        List<NotificationsDTO> expectedDTOs = notificationsList.stream()
                .map(n -> buildNotificationsDTO(n, userInfoDTO))
                .toList();


        when(notificationsRepository.findByAuthorId(eq(user.getId()), any(Pageable.class))).thenReturn(new PageImpl<>(notificationsList));
        when(notificationsRepository.saveAll(anyCollection())).thenReturn(notificationsList);

        // Act
        List<NotificationsDTO> result = notificationsService.markAllAsRead(user.getId());

        // Assert
        verify(notificationsRepository).findByAuthorId(eq(user.getId()), any(Pageable.class));
        verify(notificationsRepository).saveAll(anyCollection());
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedDTOs);
        assertTrue(result.stream().allMatch(NotificationsDTO::isRead));
    }
}
