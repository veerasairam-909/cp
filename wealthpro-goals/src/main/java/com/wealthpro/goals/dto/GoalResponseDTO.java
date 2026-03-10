package com.wealthpro.goals.dto;

import com.wealthpro.goals.entity.Goal.GoalType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalResponseDTO {
    private Long goalId;
    private Long clientId;
    private GoalType goalType;
    private BigDecimal targetAmount;
    private LocalDate targetDate;
    private Integer priority;
    private String status;
}
