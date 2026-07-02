package com.anikettcodes.healthjournal.dto;

import java.math.BigDecimal;

public record LlmFormatDto(
    Integer parsedCalories,
    BigDecimal parsedProtein,
    BigDecimal parsedCarbs,
    BigDecimal parsedFat,
    Float status,
    String errorMessage
) {
    public static LlmFormatDto of(String errorMessage){
        return new LlmFormatDto(null, null, null, null, 0f, errorMessage);
    }
}
