package com.wealthpro.goals.dto;

import com.wealthpro.goals.enums.RecommendationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendationDTO {
    
    private Long recoId;

    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotNull(message = "Model Portfolio ID is required")
    private Long modelId;

    private ModelPortfolioDTO modelPortfolio; // Populated on responses
    
    @NotBlank(message = "Proposal JSON is required")
    private String proposalJson;
    
    private LocalDateTime proposedDate;

    @NotNull(message = "Status is required")
    private RecommendationStatus status;
}
