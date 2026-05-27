package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.util.WeightRange;

import java.util.List;

public record WeightAnalyticsResponse(
        WeightRange range,
        String resolution,
        List<WeightDataPoint> dataPoints,
        int realPointCount
) {}