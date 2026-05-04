package com.anikettcodes.healthjournal.domain;

import com.anikettcodes.healthjournal.util.MealType;
import com.anikettcodes.healthjournal.util.SourceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "macro_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacroLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;

    @Column(name = "llm_comment", columnDefinition = "TEXT")
    private String llmComment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType source;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}