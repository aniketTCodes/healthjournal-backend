package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.MacroLog;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MacroLogRepository extends JpaRepository<MacroLog, Long> {

    // Used by findAll (date range) — existing
    List<MacroLog> findByUserIdAndDateBetweenOrderByDateDescCreatedAtDesc(
            Long userId, LocalDate from, LocalDate to);

    // Used by getDailySummary (single day) — new
    List<MacroLog> findByUserIdAndDateOrderByCreatedAtDesc(
            Long userId, LocalDate date);


    List<MacroLog> findByUserIdAndDateOrderByDateDesc(Long userId, LocalDate date);
}