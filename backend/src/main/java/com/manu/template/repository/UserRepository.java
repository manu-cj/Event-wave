package com.manu.template.repository;

import com.beust.jcommander.internal.Nullable;
import com.manu.template.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);

    // Search user with many params
    @Query("SELECT u FROM User u WHERE " +
            "(:param IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT(:param, '%')) " +
            "OR LOWER(u.lastname) LIKE LOWER(CONCAT(:param, '%')) " +
            "OR LOWER(u.firstname) LIKE LOWER(CONCAT(:param, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT(:param, '%')))")
    Page<User> searchBySingleParam(
            @Nullable String param,
            Pageable pageable
    );

}