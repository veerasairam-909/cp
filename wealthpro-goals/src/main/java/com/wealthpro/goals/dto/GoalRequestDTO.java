package com.wealthpro.goals.dto;

import com.wealthpro.goals.enums.GoalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalRequestDTO {
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotNull(message = "Goal Type is required")
    private GoalType goalType;
    
    @NotNull(message = "Target Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Target amount must be positive")
    private BigDecimal targetAmount;
    
    @FutureOrPresent(message = "Target date must be in present or future")
    private LocalDate targetDate;
    
    private Integer priority;
    
    @NotNull(message = "Status is required")
    private String status;
}
