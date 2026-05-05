package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.dto.AuthResponse;
import com.anikettcodes.healthjournal.dto.LoginRequest;
import com.anikettcodes.healthjournal.dto.RegisterUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Value("${jwt.expiration-ms:86400000}") // 24h default
    private long expirationMs;

    public AuthResponse login(LoginRequest request){
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.loadUser(request.getUsername());
        String accessToken = jwtService.generateToken(request.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(request.getUsername(),user.getId());

        return new AuthResponse(
                accessToken,
                refreshToken,
                expirationMs,
                "Bearer",
                user.getEmail()
        );
    }

    public AuthResponse registerUser(RegisterUserRequest registerUserRequest) {

        if(!userService.checkUserExistsByEmail(registerUserRequest.getUsername())){

        }
        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());

        User user = User.builder()
                .email(registerUserRequest.getUsername())
                .passwordHash(encodedPassword)
                .name(registerUserRequest.getName())
                .dateOfBirth(registerUserRequest.getDateOfBirth())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .heightCm(registerUserRequest.getHeightCm())
                .goal(registerUserRequest.getGoal())
                .activityLevel(registerUserRequest.getActivityLevel())
                .build();

        user = userService.saveUser(user);

        return login(new LoginRequest(registerUserRequest.getUsername(), registerUserRequest.getPassword()));

    }

}
