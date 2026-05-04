package com.anikettcodes.healthjournal.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WeightLogPatchRequest {

    @Positive(message = "Weight must be positive")
    private Double weight;
}