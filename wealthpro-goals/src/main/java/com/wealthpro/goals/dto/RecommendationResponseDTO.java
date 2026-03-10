package com.wealthpro.goals.dto;

import com.wealthpro.goals.entity.Recommendation.RecommendationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendationResponseDTO {
    private Long recoId;
    private Long clientId;
    private ModelPortfolioResponseDTO modelPortfolio;
    private String proposalJson;
    private LocalDateTime proposedDate;
    private RecommendationStatus status;
}
