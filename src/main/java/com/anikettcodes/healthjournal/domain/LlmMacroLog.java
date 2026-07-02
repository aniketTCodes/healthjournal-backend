package com.anikettcodes.healthjournal.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "llm_macro_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmMacroLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- LLM Data ---
    @Column(name = "user_input", nullable = false, columnDefinition = "TEXT")
    private String userInput;

    @Column(name = "llm_response", columnDefinition = "TEXT")
    private String llmResponse;

    // --- Parsed Nutrition ---
    @Column(name = "parsed_calories")
    private Integer parsedCalories;

    @Column(name = "parsed_protein", precision = 6, scale = 2)
    private BigDecimal parsedProtein;

    @Column(name = "parsed_carbs", precision = 6, scale = 2)
    private BigDecimal parsedCarbs;

    @Column(name = "parsed_fat", precision = 6, scale = 2)
    private BigDecimal parsedFat;

    // --- Metadata ---
    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "success";

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "latency")
    private Integer latency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Lifecycle Hooks ---
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "success";
        }
    }
}