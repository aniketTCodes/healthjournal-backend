package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.LoginRequest;
import com.anikettcodes.healthjournal.dto.AuthResponse;
import com.anikettcodes.healthjournal.service.JwtService;
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

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(jwtService.login(loginRequest));
    }

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest)



}
