package com.anikettcodes.healthjournal.dto;

import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.util.ActivityLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {

    private Long id;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private Double heightCm;
    private ActivityLevel activityLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .heightCm(user.getHeightCm())
                .activityLevel(user.getActivityLevel())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
