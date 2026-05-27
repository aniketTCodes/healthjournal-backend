package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.WeightLogPatchRequest;
import com.anikettcodes.healthjournal.dto.WeightLogRequest;
import com.anikettcodes.healthjournal.dto.WeightLogResponse;
// TODO: uncomment when security is implemented
// import com.anikettcodes.healthjournal.security.AppUserPrincipal;
import com.anikettcodes.healthjournal.security.AppUserPrinciple;
import com.anikettcodes.healthjournal.service.WeightLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class WeightLogController {

    private final WeightLogService weightLogService;


    // POST /me/weight-logs
    @PostMapping("/me/weight-logs")
    public ResponseEntity<WeightLogResponse> create(
            Authentication authentication,
            @Valid @RequestBody WeightLogRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weightLogService.create(Long.parseLong(authentication.getPrincipal().toString()), request));
    }

    // GET /me/weight-logs?from=&to=
    @GetMapping("/me/weight-logs")
    public ResponseEntity<List<WeightLogResponse>> findAll(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(weightLogService.findAll(Long.parseLong(authentication.getPrincipal().toString()), from, to));
    }

    // PATCH /weight-logs/{id}
    @PatchMapping("/weight-logs/{id}")
    public ResponseEntity<WeightLogResponse> patch(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody WeightLogPatchRequest request
    ) {
        return ResponseEntity.ok(weightLogService.patch(Long.parseLong(authentication.getPrincipal().toString()), id, request));
    }

    // DELETE /weight-logs/{id}
    @DeleteMapping("/weight-logs/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable Long id
    ) {
        weightLogService.delete(Long.parseLong(authentication.getPrincipal().toString()), id);
        return ResponseEntity.noContent().build();
    }
}