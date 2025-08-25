package com.manu.template.repository;

import com.manu.template.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogsRepository extends JpaRepository<Logs, UUID> {
}