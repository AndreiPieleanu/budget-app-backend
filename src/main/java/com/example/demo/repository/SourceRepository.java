package com.example.demo.repository;

import com.example.demo.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Integer> {
    @Query("SELECT s FROM Source s WHERE s.sheet.id = :sheetId")
    List<Source> findBySheetId(@Param("sheetId") Integer sheetId);
}
