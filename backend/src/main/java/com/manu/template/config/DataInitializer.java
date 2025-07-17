package com.manu.template.config;

import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("manu@gmail.com").isEmpty()) {
                User user = User.builder()
                        .username("Manu-cj")
                        .email("manu@gmail.com")
                        .firstname("Manu")
                        .lastname("Crj")
                        .password(passwordEncoder.encode("motdepasse"))
                        .roles(Collections.singleton("ADMIN"))
                        .build();
                userRepository.save(user);
            }
        };
    }

}
