package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.MacroLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MacroLogRepository extends JpaRepository<MacroLog, Long> {

    List<MacroLog> findByUserIdAndDateBetweenOrderByDateDescCreatedAtDesc(
            Long userId,
            LocalDate from,
            LocalDate to
    );
}
