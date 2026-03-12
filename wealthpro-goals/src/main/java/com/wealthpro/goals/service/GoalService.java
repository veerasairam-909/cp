package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.GoalRequestDTO;
import com.wealthpro.goals.dto.GoalResponseDTO;

import java.util.List;

public interface GoalService {
    GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO);
    List<GoalResponseDTO> getGoalsByClientId(Long clientId);
    GoalResponseDTO updateGoalStatus(Long goalId, String status);
}
