package com.anikettcodes.healthjournal.dto;

import java.time.LocalDate;
import java.util.List;

public record DailySummaryResponse(
        LocalDate date,
        MacroTotals totals,
        GoalSnapshot goals,          // null until goal system is built
        List<MacroLogResponse> entries
) {
    public static DailySummaryResponse of(LocalDate date, List<MacroLogResponse> entries) {
        return new DailySummaryResponse(
                date,
                MacroTotals.from(entries),
                null,
                entries
        );
    }
}