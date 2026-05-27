package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    // existing — untouched
    List<WeightLog> findByUserIdAndLoggedAtBetweenOrderByLoggedAtDesc(
            Long userId,
            LocalDate from,
            LocalDate to
    );

    // existing — untouched
    Optional<WeightLog> findByUserIdAndLoggedAt(Long userId, LocalDate loggedAt);

    // new — last log before a given date (LOCF seed from outside window)
    Optional<WeightLog> findTopByUserIdAndLoggedAtBeforeOrderByLoggedAtDesc(
            Long userId,
            LocalDate date
    );

    // new — earliest log ever (backward projection for new users)
    Optional<WeightLog> findTopByUserIdOrderByLoggedAtAsc(Long userId);

    // new — all logs in range, ascending (used by analytics interpolation)
    List<WeightLog> findByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(
            Long userId,
            LocalDate from,
            LocalDate to
    );
}