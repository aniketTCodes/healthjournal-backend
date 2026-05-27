package com.anikettcodes.healthjournal.dto;

import java.util.List;

public record MacroTotals(
        double calories,
        double protein,
        double carbs,
        double fat
) {
    public static MacroTotals from(List<MacroLogResponse> entries) {
        return new MacroTotals(
                entries.stream().mapToDouble(MacroLogResponse::getCalories).sum(),
                entries.stream().mapToDouble(MacroLogResponse::getProtein).sum(),
                entries.stream().mapToDouble(MacroLogResponse::getCarbs).sum(),
                entries.stream().mapToDouble(MacroLogResponse::getFat).sum()
        );
    }
}