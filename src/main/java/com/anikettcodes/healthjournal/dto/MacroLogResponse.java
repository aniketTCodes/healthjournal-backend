package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.domain.MacroLog;
import com.anikettcodes.healthjournal.util.MealType;
import com.anikettcodes.healthjournal.util.SourceType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
public class MacroLogResponse {

    private Long id;
    private LocalDate date;
    private MealType mealType;
    private String description;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private String llmComment;
    private SourceType source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MacroLogResponse from(MacroLog log) {
        return MacroLogResponse.builder()
                .id(log.getId())
                .date(log.getDate())
                .mealType(log.getMealType())
                .description(log.getDescription())
                .calories(log.getCalories())
                .protein(log.getProtein())
                .carbs(log.getCarbs())
                .fat(log.getFat())
                .llmComment(log.getLlmComment())
                .source(log.getSource())
                .createdAt(log.getCreatedAt())
                .updatedAt(log.getUpdatedAt())
                .build();
    }


}
