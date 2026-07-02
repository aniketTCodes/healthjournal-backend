package com.anikettcodes.healthjournal.dto;


import com.anikettcodes.healthjournal.util.MealType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LlmMacroLogRequest {

    @NotNull(message = "date is required")
    private String user_input;

    @NotNull(message = "mealType is required")
    private MealType mealType;


}
