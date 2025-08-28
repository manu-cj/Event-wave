package com.manu.template.repository;

import com.manu.template.model.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
    Page<Notifications> findByAuthorId(UUID id, Pageable pageable);
}