package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.util.MealType;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MacroLogPatchRequest {

    private MealType mealType;

    private String description;

    @PositiveOrZero
    private Double calories;

    @PositiveOrZero
    private Double protein;

    @PositiveOrZero
    private Double carbs;

    @PositiveOrZero
    private Double fat;
}
