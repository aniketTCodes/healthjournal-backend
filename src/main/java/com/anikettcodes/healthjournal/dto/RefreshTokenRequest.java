package com.anikettcodes.healthjournal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}