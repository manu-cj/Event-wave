package com.manu.template.service;

import com.manu.template.repository.NotificationsRepository;
import com.manu.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsRepository notificationsRepository;
    private final UserRepository userRepository;


}
