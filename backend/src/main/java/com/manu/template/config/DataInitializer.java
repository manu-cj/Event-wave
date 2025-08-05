package com.manu.template.config;

import com.manu.template.model.Event;
import com.manu.template.model.User;
import com.manu.template.repository.EventRepository;
import com.manu.template.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                      EventRepository eventRepository) {
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

            if (eventRepository.findByTitle("Confèrence Angular").isEmpty()) {
                Event event = Event.builder()
                        .title("Confèrence Angular")
                        .description("Une confèrence Exceptionnel sur Angular présenté par Manu")
                        .date(LocalDateTime.now())
                        .city("Charleroi")
                        .postalCode(6000)
                        .address("Sq. des Martyrs 1")
                        .availablePlaces(8)
                        .pictureUrl("/uploads/2a7f4250-43f0-4342-844e-945f2e86783b_5073772lpaw-5073794-article-jpg_3732696_1250x625.jpg")
                        .phoneNumber("0491/.11.11.11")
                        .emailAddress("manu@gmail.com")
                        .build();
                eventRepository.save(event);
            }

            if (eventRepository.findByTitle("Atelier Spring Boot").isEmpty()) {
                Event event = Event.builder()
                        .title("Atelier Spring Boot")
                        .description("Atelier pratique pour découvrir les bases de Spring Boot.")
                        .date(LocalDateTime.now().plusDays(7))
                        .city("Bruxelles")
                        .postalCode(1000)
                        .address("Rue de la Loi 16")
                        .availablePlaces(15)
                        .pictureUrl("/uploads/spring-boot-workshop.png")
                        .phoneNumber("0488/22.33.44")
                        .emailAddress("atelier@springboot.com")
                        .build();
                eventRepository.save(event);
            }
            if (eventRepository.findByTitle("Hackathon Java").isEmpty()) {
                Event event = Event.builder()
                        .title("Hackathon Java")
                        .description("Participez à un hackathon intensif pour développer des applications Java innovantes.")
                        .date(LocalDateTime.now().plusDays(14))
                        .city("Liège")
                        .postalCode(4000)
                        .address("Boulevard d'Avroy 25")
                        .availablePlaces(20)
                        .pictureUrl("/uploads/hackathon-java.jpg")
                        .phoneNumber("0477/55.66.77")
                        .emailAddress("hackathon@java.com")
                        .build();
                eventRepository.save(event);
            }
        };
    }

}
