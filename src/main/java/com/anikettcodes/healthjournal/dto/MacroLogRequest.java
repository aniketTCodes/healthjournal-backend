package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.util.MealType;
import com.anikettcodes.healthjournal.util.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MacroLogRequest {

    @NotNull(message = "date is required")
    private LocalDate date;

    @NotNull(message = "mealType is required")
    private MealType mealType;

    @NotBlank(message = "description is required")
    private String description;

    @PositiveOrZero
    private Double calories;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double carbs;

    @PositiveOrZero
    private Double fat;

    private String llmComment;

    @NotNull(message = "source is required")
    private SourceType source;
}
