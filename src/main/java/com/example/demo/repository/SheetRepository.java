package com.example.demo.repository;

import com.example.demo.entity.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheetRepository extends JpaRepository<Sheet, Integer> {
}
