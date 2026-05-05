package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.util.ActivityLevel;
import com.anikettcodes.healthjournal.util.FitnessGoal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {

    @NotNull(message =  "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Confirm password cannot be null")
    private String confirmPassword;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Date cannot be null")
    private LocalDate dateOfBirth;

    @Positive
    @NotNull(message = "Height cannot be null")
    private Double heightCm;

    private ActivityLevel activityLevel;
    private FitnessGoal goal;

}
