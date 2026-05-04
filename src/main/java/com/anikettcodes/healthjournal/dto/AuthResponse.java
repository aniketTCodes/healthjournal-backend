package com.anikettcodes.healthjournal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class AuthResponse {

    public String accessToken;
    public String refreshToken;
    public long expiresIn;
    public String tokenType;
    public String username;

    public AuthResponse(String refreshToken, String accessToken, long expiresIn, String tokenType, String username) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.username = username;
    }
}
