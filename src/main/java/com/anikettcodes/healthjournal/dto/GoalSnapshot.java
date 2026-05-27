package com.anikettcodes.healthjournal.dto;

/**
 * Populated once the goal system is implemented.
 * Intentionally null in DailySummaryResponse until then —
 * the Flutter client should null-check before rendering progress bars.
 */
public record GoalSnapshot(
        Double calories,
        Double protein,
        Double carbs,
        Double fat
) {}