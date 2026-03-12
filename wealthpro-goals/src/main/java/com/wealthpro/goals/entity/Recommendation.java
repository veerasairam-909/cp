package com.wealthpro.goals.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.wealthpro.goals.enums.RecommendationStatus;

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
    private RecommendationStatus status;
}
