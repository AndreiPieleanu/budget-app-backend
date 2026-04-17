package com.example.demo.repository;

import com.example.demo.dto.SheetDTO;
import com.example.demo.entity.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheetRepository extends JpaRepository<Sheet, Integer> {
    List<Sheet> findByUserId(Integer user_id);
}
