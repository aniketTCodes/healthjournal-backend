package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.LoginRequest;
import com.anikettcodes.healthjournal.dto.AuthResponse;
import com.anikettcodes.healthjournal.dto.RefreshTokenRequest;
import com.anikettcodes.healthjournal.dto.RegisterUserRequest;
import com.anikettcodes.healthjournal.service.AuthService;
import com.anikettcodes.healthjournal.service.JwtService;
import com.anikettcodes.healthjournal.service.WeightLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final WeightLogService weightLogService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterUserRequest registerRequest) {

        ResponseEntity<AuthResponse> response = ResponseEntity.ok(authService.registerUser(registerRequest));
        return response;
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }


}
