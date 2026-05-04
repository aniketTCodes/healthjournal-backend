package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.UserProfilePatchRequest;
import com.anikettcodes.healthjournal.dto.UserProfileResponse;
// TODO: uncomment when security is implemented
// import com.anikettcodes.healthjournal.security.AppUserPrincipal;
import com.anikettcodes.healthjournal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO: replace hardcoded ID with @AuthenticationPrincipal AppUserPrincipal principal
    private static final Long HARDCODED_USER_ID = 1L;

    // GET /me/profile
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            // @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return ResponseEntity.ok(userService.getProfile(HARDCODED_USER_ID));
    }

    // PATCH /me/profile
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResponse> patchProfile(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @Valid @RequestBody UserProfilePatchRequest request
    ) {
        return ResponseEntity.ok(userService.patchProfile(HARDCODED_USER_ID, request));
    }
}
