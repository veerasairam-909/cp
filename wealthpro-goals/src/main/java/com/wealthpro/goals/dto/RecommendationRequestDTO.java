package com.wealthpro.goals.dto;

import com.wealthpro.goals.enums.RecommendationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecommendationRequestDTO {
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotNull(message = "Model Portfolio ID is required")
    private Long modelId;
    
    @NotBlank(message = "Proposal JSON is required")
    private String proposalJson;
    
    @NotNull(message = "Status is required")
    private RecommendationStatus status;
}
