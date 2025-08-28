package com.manu.template.mapper;

import com.manu.template.dto.NotificationsDTO;
import com.manu.template.model.Notifications;

public class NotificationsMapper {
    public static NotificationsDTO toDto(Notifications notifications) {
        if (notifications == null) return null;
        return NotificationsDTO.builder()
                .id(notifications.getId())
                .author(UserMapper.toDto(notifications.getAuthor()))
                .read(notifications.isRead())
                .description(notifications.getDescription())
                .createdAt(notifications.getCreatedAt())
                .build();
    }

    public static Notifications toEntity(NotificationsDTO notificationsDTO) {
        if (notificationsDTO == null) return null;
        return Notifications.builder()
                .id(notificationsDTO.getId())
                .author(UserMapper.toEntity(notificationsDTO.getAuthor()))
                .read(notificationsDTO.isRead())
                .description(notificationsDTO.getDescription())
                .createdAt(notificationsDTO.getCreatedAt())
                .build();
    }
}
