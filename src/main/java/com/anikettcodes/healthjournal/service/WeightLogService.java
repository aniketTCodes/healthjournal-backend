package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.domain.WeightLog;
import com.anikettcodes.healthjournal.dto.WeightLogPatchRequest;
import com.anikettcodes.healthjournal.dto.WeightLogRequest;
import com.anikettcodes.healthjournal.dto.WeightLogResponse;
import com.anikettcodes.healthjournal.repository.UserRepository;
import com.anikettcodes.healthjournal.repository.WeightLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public WeightLogResponse create(Long userId, WeightLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        WeightLog log = WeightLog.builder()
                .user(user)
                .weight(request.getWeight())
                .loggedAt(request.getLoggedAt())
                .createdAt(LocalDateTime.now())
                .build();

        return WeightLogResponse.from(weightLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<WeightLogResponse> findAll(Long userId, LocalDateTime from, LocalDateTime to) {
        return weightLogRepository
                .findByUserIdAndLoggedAtBetweenOrderByLoggedAtDesc(userId, from, to)
                .stream()
                .map(WeightLogResponse::from)
                .toList();
    }

    @Transactional
    public WeightLogResponse patch(Long userId, Long logId, WeightLogPatchRequest request) {
        WeightLog log = weightLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("WeightLog not found: " + logId));

        if (!log.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied to weight log: " + logId);
        }

        if (request.getWeight() != null) {
            log.setWeight(request.getWeight());
        }

        return WeightLogResponse.from(weightLogRepository.save(log));
    }

    @Transactional
    public void delete(Long userId, Long logId) {
        WeightLog log = weightLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("WeightLog not found: " + logId));

        if (!log.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied to weight log: " + logId);
        }

        weightLogRepository.delete(log);
    }
}