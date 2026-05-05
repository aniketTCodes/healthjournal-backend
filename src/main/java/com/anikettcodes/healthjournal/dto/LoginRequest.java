package com.anikettcodes.healthjournal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
