package com.anikettcodes.healthjournal.dto;

import java.time.LocalDate;

public record WeightDataPoint(
        LocalDate date,
        Double weight,      // null = line break point
        boolean isImputed
) {}