package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.MacroLogPatchRequest;
import com.anikettcodes.healthjournal.dto.MacroLogRequest;
import com.anikettcodes.healthjournal.dto.MacroLogResponse;
// TODO: uncomment when security is implemented
// import com.anikettcodes.healthjournal.security.AppUserPrincipal;
import com.anikettcodes.healthjournal.service.MacroLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MacroLogController {

    private final MacroLogService macroLogService;

    // TODO: replace hardcoded ID with @AuthenticationPrincipal AppUserPrincipal principal
    private static final Long HARDCODED_USER_ID = 1L;

    // POST /me/food-entries
    @PostMapping("/me/food-entries")
    public ResponseEntity<MacroLogResponse> create(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @Valid @RequestBody MacroLogRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(macroLogService.create(HARDCODED_USER_ID, request));
    }

    // GET /me/food-entries?from=&to=
    @GetMapping("/me/food-entries")
    public ResponseEntity<List<MacroLogResponse>> findAll(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(macroLogService.findAll(HARDCODED_USER_ID, from, to));
    }

    // PATCH /food-entries/{id}
    @PatchMapping("/food-entries/{id}")
    public ResponseEntity<MacroLogResponse> patch(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody MacroLogPatchRequest request
    ) {
        return ResponseEntity.ok(macroLogService.patch(HARDCODED_USER_ID, id, request));
    }

    // DELETE /food-entries/{id}
    @DeleteMapping("/food-entries/{id}")
    public ResponseEntity<Void> delete(
            // @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long id
    ) {
        macroLogService.delete(HARDCODED_USER_ID, id);
        return ResponseEntity.noContent().build();
    }
}
