package com.anikettcodes.healthjournal.controller;

import com.anikettcodes.healthjournal.dto.WeightAnalyticsResponse;
import com.anikettcodes.healthjournal.util.WeightRange;
import com.anikettcodes.healthjournal.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/weight")
    public ResponseEntity<WeightAnalyticsResponse> getWeightAnalytics(
            Authentication authentication,
            @RequestParam WeightRange range
    ) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        return ResponseEntity.ok(analyticsService.getWeightSeries(userId, range));
    }
}