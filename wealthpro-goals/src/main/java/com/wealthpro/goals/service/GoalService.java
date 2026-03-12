package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.GoalDTO;

import java.util.List;

public interface GoalService {
    GoalDTO createGoal(GoalDTO goalDTO);
    List<GoalDTO> getGoalsByClientId(Long clientId);
    GoalDTO updateGoalStatus(Long goalId, String status);
}
