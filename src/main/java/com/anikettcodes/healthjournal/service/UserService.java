package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.User;
import com.anikettcodes.healthjournal.dto.UserProfilePatchRequest;
import com.anikettcodes.healthjournal.dto.UserProfileResponse;
import com.anikettcodes.healthjournal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = findById(userId);
        return UserProfileResponse.from(user);
    }

    @Transactional
    public UserProfileResponse patchProfile(Long userId, UserProfilePatchRequest request) {
        User user = findById(userId);

        if (request.getName() != null) user.setName(request.getName());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if (request.getHeightCm() != null) user.setHeightCm(request.getHeightCm());
        if (request.getActivityLevel() != null) user.setActivityLevel(request.getActivityLevel());

        user.setUpdatedAt(LocalDateTime.now());

        return UserProfileResponse.from(userRepository.save(user));
    }

    // --- helpers ---

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User does not exists"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPasswordHash(), List.of());
    }


    @NonNull
    public User loadUser(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User does not exists"));
    }
}
