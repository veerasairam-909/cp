package com.wealthpro.goals.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModelPortfolioRequestDTO {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Risk Class is required")
    private String riskClass;
    
    @NotBlank(message = "Weights JSON is required")
    private String weightsJson;
    
    @NotBlank(message = "Status is required")
    private String status;
}
