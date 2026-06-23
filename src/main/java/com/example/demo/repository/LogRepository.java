package com.example.demo.repository;

import com.example.demo.entity.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<LogEvent, Long> {
    List<LogEvent> findByUserId(Integer user_id);
}
