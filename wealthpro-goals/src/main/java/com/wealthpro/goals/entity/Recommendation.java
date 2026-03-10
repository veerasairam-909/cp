package com.wealthpro.goals.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recoId;

    @Column(nullable = false)
    private Long clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelPortfolio modelPortfolio;

    @Column(columnDefinition = "json")
    private String proposalJson;

    private LocalDateTime proposedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationStatus status;

    public enum RecommendationStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED
    }
}
