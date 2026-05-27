package com.anikettcodes.healthjournal.service;

import com.anikettcodes.healthjournal.domain.WeightLog;
import com.anikettcodes.healthjournal.util.WeightRange;
import com.anikettcodes.healthjournal.dto.WeightAnalyticsResponse;
import com.anikettcodes.healthjournal.dto.WeightDataPoint;
import com.anikettcodes.healthjournal.repository.WeightLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final WeightLogRepository weightLogRepository;

    @Transactional(readOnly = true)
    public WeightAnalyticsResponse getWeightSeries(Long userId, WeightRange range) {
        return range == WeightRange.YEARLY
                ? buildYearlySeries(userId)
                : buildDailySeries(userId, range);
    }

    // -------------------------------------------------------------------------
    // DAILY resolution (WEEKLY and MONTHLY views)
    // -------------------------------------------------------------------------

    private WeightAnalyticsResponse buildDailySeries(Long userId, WeightRange range) {
        LocalDate to   = LocalDate.now();
        LocalDate from = to.minus(range.windowSize, ChronoUnit.DAYS);

        List<WeightLog> logs = weightLogRepository
                .findByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(userId, from, to);

        // Build date → weight map from real logs
        Map<LocalDate, Double> logMap = new LinkedHashMap<>();
        for (WeightLog log : logs) {
            logMap.put(log.getLoggedAt(), log.getWeight());
        }

        // Find seed: last log before window, or earliest log ever (backward projection)
        Double seed = weightLogRepository
                .findTopByUserIdAndLoggedAtBeforeOrderByLoggedAtDesc(userId, from)
                .map(WeightLog::getWeight)
                .orElseGet(() ->
                        weightLogRepository
                                .findTopByUserIdOrderByLoggedAtAsc(userId)
                                .map(WeightLog::getWeight)
                                .orElse(null)
                );

        List<WeightDataPoint> dataPoints = new ArrayList<>();

        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            if (logMap.containsKey(d)) {
                // Real log — override with actual value
                dataPoints.add(new WeightDataPoint(d, logMap.get(d), false));
                seed = logMap.get(d); // update seed so forward carry uses latest real value
            } else {
                // No log — fill with seed (null if user has zero logs anywhere)
                dataPoints.add(new WeightDataPoint(d, seed, seed != null));
            }
        }

        int realPointCount = (int) dataPoints.stream()
                .filter(p -> !p.isImputed())
                .count();

        return new WeightAnalyticsResponse(range, "DAILY", dataPoints, realPointCount);
    }

    // -------------------------------------------------------------------------
    // WEEKLY bucket resolution (YEARLY view)
    // -------------------------------------------------------------------------

    private WeightAnalyticsResponse buildYearlySeries(Long userId) {
        LocalDate to   = LocalDate.now();
        LocalDate from = to.minus(52, ChronoUnit.WEEKS);

        List<WeightLog> logs = weightLogRepository
                .findByUserIdAndLoggedAtBetweenOrderByLoggedAtAsc(userId, from, to);

        // Group logs by ISO week, then average each bucket
        Map<String, List<Double>> weekBuckets = new LinkedHashMap<>();
        for (WeightLog log : logs) {
            String key = isoWeekKey(log.getLoggedAt());
            weekBuckets.computeIfAbsent(key, k -> new ArrayList<>()).add(log.getWeight());
        }

        Map<String, Double> weeklyAverages = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : weekBuckets.entrySet()) {
            double avg = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            weeklyAverages.put(entry.getKey(), avg);
        }

        // Find seed: last log before window, or earliest log ever (backward projection)
        Double seed = weightLogRepository
                .findTopByUserIdAndLoggedAtBeforeOrderByLoggedAtDesc(userId, from)
                .map(WeightLog::getWeight)
                .orElseGet(() ->
                        weightLogRepository
                                .findTopByUserIdOrderByLoggedAtAsc(userId)
                                .map(WeightLog::getWeight)
                                .orElse(null)
                );

        List<WeightDataPoint> dataPoints = new ArrayList<>();

        LocalDate weekStart = from;
        while (!weekStart.isAfter(to)) {
            String key = isoWeekKey(weekStart);

            if (weeklyAverages.containsKey(key)) {
                // Real bucket — override with actual average
                dataPoints.add(new WeightDataPoint(weekStart, weeklyAverages.get(key), false));
                seed = weeklyAverages.get(key); // update seed for forward carry
            } else {
                // No logs this week — fill with seed
                dataPoints.add(new WeightDataPoint(weekStart, seed, seed != null));
            }

            weekStart = weekStart.plusWeeks(1);
        }

        int realPointCount = (int) dataPoints.stream()
                .filter(p -> !p.isImputed())
                .count();

        return new WeightAnalyticsResponse(WeightRange.YEARLY, "WEEKLY", dataPoints, realPointCount);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private String isoWeekKey(LocalDate date) {
        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = date.get(IsoFields.WEEK_BASED_YEAR);
        return year + "-" + String.format("%02d", week);
    }
}