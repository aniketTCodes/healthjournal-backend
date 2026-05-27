package com.anikettcodes.healthjournal.util;

import java.time.temporal.ChronoUnit;

public enum WeightRange {
    WEEKLY(7, ChronoUnit.DAYS, 2),
    MONTHLY(30, ChronoUnit.DAYS, 7),
    YEARLY(52, ChronoUnit.WEEKS, 3);

    public final int windowSize;
    public final ChronoUnit resolution;
    public final int maxCarryUnits;

    WeightRange(int windowSize, ChronoUnit resolution, int maxCarryUnits) {
        this.windowSize = windowSize;
        this.resolution = resolution;
        this.maxCarryUnits = maxCarryUnits;
    }
}