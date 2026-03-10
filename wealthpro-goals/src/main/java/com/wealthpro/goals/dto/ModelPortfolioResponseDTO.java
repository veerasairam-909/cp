package com.wealthpro.goals.dto;

import lombok.Data;

@Data
public class ModelPortfolioResponseDTO {
    private Long modelId;
    private String name;
    private String riskClass;
    private String weightsJson;
    private String status;
}
