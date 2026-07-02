package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.LlmFormatDto;
import com.anikettcodes.healthjournal.dto.*;
import com.anikettcodes.healthjournal.service.AiMacroLogService;
import com.anikettcodes.healthjournal.service.MacroLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MacroLogController {

    private final MacroLogService macroLogService;
    private final AiMacroLogService aiMacroLogService;

    @PostMapping("/me/food-entries/ai")
    public LLMResponseDto createAiFoodEntry(
            Authentication authentication,
            @RequestBody String prompt
    ) {
        return aiMacroLogService.getMacroLog(prompt,userId(authentication));
    }

    // POST /me/food-entries
    @PostMapping("/me/food-entries")
    public ResponseEntity<MacroLogResponse> create(
            Authentication authentication,
            @Valid @RequestBody MacroLogRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(macroLogService.create(userId(authentication), request));
    }

    // GET /me/food-entries?from=&to=
    @GetMapping("/me/food-entries")
    public ResponseEntity<List<MacroLogResponse>> findAll(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(macroLogService.findAll(userId(authentication), date));
    }

    // GET /me/daily-summary?date=   (defaults to today if omitted)
    @GetMapping("/me/daily-summary")
    public ResponseEntity<DailySummaryResponse> getDailySummary(
            Authentication authentication,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(macroLogService.getDailySummary(userId(authentication), targetDate));
    }

    // PATCH /food-entries/{id}
    @PatchMapping("/food-entries/{id}")
    public ResponseEntity<MacroLogResponse> patch(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody MacroLogPatchRequest request
    ) {
        return ResponseEntity.ok(macroLogService.patch(userId(authentication), id, request));
    }

    // DELETE /food-entries/{id}
    @DeleteMapping("/food-entries/{id}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable Long id
    ) {
        macroLogService.delete(userId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    private Long userId(Authentication authentication) {
        return Long.parseLong(authentication.getPrincipal().toString());
    }
}