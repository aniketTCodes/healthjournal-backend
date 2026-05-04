package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.MacroLog;
import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.dto.MacroLogPatchRequest;
import com.anikettcodes.healthjournal.dto.MacroLogRequest;
import com.anikettcodes.healthjournal.dto.MacroLogResponse;
import com.anikettcodes.healthjournal.repository.MacroLogRepository;
import com.anikettcodes.healthjournal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MacroLogService {

    private final MacroLogRepository macroLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public MacroLogResponse create(Long userId, MacroLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        MacroLog log = MacroLog.builder()
                .user(user)
                .date(request.getDate())
                .mealType(request.getMealType())
                .description(request.getDescription())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fat(request.getFat())
                .llmComment(request.getLlmComment())
                .source(request.getSource())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return MacroLogResponse.from(macroLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<MacroLogResponse> findAll(Long userId, LocalDate from, LocalDate to) {
        return macroLogRepository
                .findByUserIdAndDateBetweenOrderByDateDescCreatedAtDesc(userId, from, to)
                .stream()
                .map(MacroLogResponse::from)
                .toList();
    }

    @Transactional
    public MacroLogResponse patch(Long userId, Long logId, MacroLogPatchRequest request) {
        MacroLog log = findOwnedLog(userId, logId);

        ensureLogIsToday(log);

        if (request.getMealType() != null) log.setMealType(request.getMealType());
        if (request.getDescription() != null) log.setDescription(request.getDescription());
        if (request.getCalories() != null) log.setCalories(request.getCalories());
        if (request.getProtein() != null) log.setProtein(request.getProtein());
        if (request.getCarbs() != null) log.setCarbs(request.getCarbs());
        if (request.getFat() != null) log.setFat(request.getFat());

        log.setUpdatedAt(LocalDateTime.now());

        return MacroLogResponse.from(macroLogRepository.save(log));
    }

    @Transactional
    public void delete(Long userId, Long logId) {
        MacroLog log = findOwnedLog(userId, logId);

        ensureLogIsToday(log);

        macroLogRepository.delete(log);
    }

    // --- helpers ---

    private MacroLog findOwnedLog(Long userId, Long logId) {
        MacroLog log = macroLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("Food entry not found: " + logId));

        if (!log.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied to food entry: " + logId);
        }

        return log;
    }

    private void ensureLogIsToday(MacroLog log) {
        if (!log.getDate().equals(LocalDate.now())) {
            throw new IllegalStateException(
                    "Only today's food entries can be modified or deleted."
            );
        }
    }
}
