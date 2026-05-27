package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.domain.WeightLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class WeightLogResponse {

    private Long id;
    private Double weight;
    private LocalDate loggedAt;
    private LocalDateTime createdAt;

    public static WeightLogResponse from(WeightLog log) {
        return WeightLogResponse.builder()
                .id(log.getId())
                .weight(log.getWeight())
                .loggedAt(log.getLoggedAt())
                .createdAt(log.getCreatedAt())
                .build();
    }
}