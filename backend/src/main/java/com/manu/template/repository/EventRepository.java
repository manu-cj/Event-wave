package com.manu.template.repository;

import com.manu.template.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
  // EventRepository.java
  List<Event> findTop3ByOrderByDateDesc();
  Page<Event> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
  Optional<Event> findByTitle(String title);
}