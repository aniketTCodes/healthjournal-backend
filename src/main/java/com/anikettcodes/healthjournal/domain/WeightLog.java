package com.anikettcodes.healthjournal.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "weight_logs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "logged_at"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "logged_at", nullable = false)
    private LocalDate loggedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}