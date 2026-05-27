package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.dto.AuthResponse;
import com.anikettcodes.healthjournal.dto.LoginRequest;
import com.anikettcodes.healthjournal.dto.RegisterUserRequest;
import com.anikettcodes.healthjournal.dto.WeightLogRequest;
import com.anikettcodes.healthjournal.exception.InvalidTokenException;
import com.anikettcodes.healthjournal.exception.PasswordMismatchedException;
import com.anikettcodes.healthjournal.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WeightLogService weightLogService;
    @Value("${jwt.expiration-ms:86400000}") // 24h default
    private long expirationMs;

    public AuthResponse login(LoginRequest request){

        User user = userService.loadUser(request.getUsername());
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String accessToken = jwtService.generateToken(request.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(request.getUsername(),user.getId());

        return new AuthResponse(
                refreshToken,
                accessToken,
                expirationMs,
                "Bearer",
                user.getEmail()
        );
    }

    @Transactional
    public AuthResponse registerUser(RegisterUserRequest registerUserRequest) {

        if(userService.checkUserExistsByEmail(registerUserRequest.getUsername())){
            throw new UserAlreadyExistsException("This user already exists");
        }

        if(!Objects.equals(registerUserRequest.getPassword(), registerUserRequest.getConfirmPassword())){
            throw new PasswordMismatchedException("Password do not matches");
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
        weightLogService.create(user.getId(), new WeightLogRequest(registerUserRequest.getInitialWeight(), LocalDate.now()));

        return login(new LoginRequest(registerUserRequest.getUsername(), registerUserRequest.getPassword()));

    }
    public AuthResponse refresh(String refreshToken) {
        // Will throw ExpiredJwtException / JwtException automatically if invalid
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Not a refresh token");
        }

        String email = jwtService.extractUsername(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);

        // Verify user still exists and is active
        User user = userService.loadUser(email);

        String newAccessToken = jwtService.generateToken(email, userId);
        // Optionally rotate the refresh token too (more secure):
        String newRefreshToken = jwtService.generateRefreshToken(email, userId);

        return new AuthResponse(newRefreshToken, newAccessToken, expirationMs, "Bearer", user.getEmail());
    }


}
