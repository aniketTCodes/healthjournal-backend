package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    List<WeightLog> findByUserIdAndLoggedAtBetweenOrderByLoggedAtDesc(
            Long userId,
            LocalDateTime from,
            LocalDateTime to
    );
}