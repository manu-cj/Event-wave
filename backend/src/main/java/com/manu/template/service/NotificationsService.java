package com.manu.template.service;

import com.manu.template.dto.NotificationsDTO;
import com.manu.template.mapper.NotificationsMapper;
import com.manu.template.model.Notifications;
import com.manu.template.model.User;
import com.manu.template.repository.NotificationsRepository;
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
public class NotificationsService {
    private final NotificationsRepository notificationsRepository;
    private final UserRepository userRepository;


    public NotificationsDTO save(NotificationsDTO notificationsDTO) {
        try {
            Notifications notifications = NotificationsMapper.toEntity(notificationsDTO);

                User user = userRepository.findById(notifications.getAuthor().getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

                notifications.setAuthor(user);

            Notifications saved = notificationsRepository.save(notifications);

            return NotificationsMapper.toDto(saved);
        }
        catch (EntityNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred when we create a notification");
        }
    }

    public Page<NotificationsDTO> findByAuthorId(UUID id, Pageable pageable) {
        return notificationsRepository.findByAuthorId(id, pageable)
                .map(NotificationsMapper::toDto);
    }

    public NotificationsDTO markAsRead(UUID id) {
        try {
            Notifications notifications = notificationsRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

            notifications.markAsRead();

            Notifications updated = notificationsRepository.save(notifications);
            return NotificationsMapper.toDto(updated);
        }
        catch (EntityNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred when marking notification as read");
        }
    }

    public List<NotificationsDTO> markAllAsRead(UUID authorId) {
        try {
            List<Notifications> notifications = notificationsRepository.findByAuthorId(authorId, Pageable.unpaged()).getContent();
            notifications.forEach(Notifications::markAsRead);
            notificationsRepository.saveAll(notifications);
            return notifications.stream()
                    .map(NotificationsMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred when updated all notifications", e);
        }
    }
}
