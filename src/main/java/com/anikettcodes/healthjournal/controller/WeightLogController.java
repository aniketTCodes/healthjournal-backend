package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.WeightLogPatchRequest;
import com.anikettcodes.healthjournal.dto.WeightLogRequest;
import com.anikettcodes.healthjournal.dto.WeightLogResponse;
// TODO: uncomment when security is implemented
// import com.anikettcodes.healthjournal.security.AppUserPrincipal;
import com.anikettcodes.healthjournal.service.WeightLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeightLogController {

    private final WeightLogService weightLogService;

    // TODO: replace hardcoded ID with @AuthenticationPrincipal AppUserPrincipal principal
    private static final Long HARDCODED_USER_ID = 1L;

    // POST /me/weight-logs
    @PostMapping("/me/weight-logs")
    public ResponseEntity<WeightLogResponse> create(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @Valid @RequestBody WeightLogRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weightLogService.create(HARDCODED_USER_ID, request));
    }

    // GET /me/weight-logs?from=&to=
    @GetMapping("/me/weight-logs")
    public ResponseEntity<List<WeightLogResponse>> findAll(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(weightLogService.findAll(HARDCODED_USER_ID, from, to));
    }

    // PATCH /weight-logs/{id}
    @PatchMapping("/weight-logs/{id}")
    public ResponseEntity<WeightLogResponse> patch(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody WeightLogPatchRequest request
    ) {
        return ResponseEntity.ok(weightLogService.patch(HARDCODED_USER_ID, id, request));
    }

    // DELETE /weight-logs/{id}
    @DeleteMapping("/weight-logs/{id}")
    public ResponseEntity<Void> delete(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long id
    ) {
        weightLogService.delete(HARDCODED_USER_ID, id);
        return ResponseEntity.noContent().build();
    }
}