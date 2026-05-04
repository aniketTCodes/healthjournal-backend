package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.util.ActivityLevel;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfilePatchRequest {

    private String name;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Positive(message = "Height must be positive")
    private Double heightCm;

    private ActivityLevel activityLevel;
}
