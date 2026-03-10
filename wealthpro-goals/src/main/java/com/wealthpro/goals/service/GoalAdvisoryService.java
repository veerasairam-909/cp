package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.*;

import java.util.List;

public interface GoalAdvisoryService {

    // Goal Methods
    GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO);
    List<GoalResponseDTO> getGoalsByClientId(Long clientId);
    GoalResponseDTO updateGoalStatus(Long goalId, String status);

    // Model Portfolio Methods
    ModelPortfolioResponseDTO createModelPortfolio(ModelPortfolioRequestDTO modelPortfolioRequestDTO);
    List<ModelPortfolioResponseDTO> getAllActiveModelPortfolios();

    // Recommendation Methods
    RecommendationResponseDTO createRecommendation(RecommendationRequestDTO recommendationRequestDTO);
    List<RecommendationResponseDTO> getRecommendationsByClientId(Long clientId);
    RecommendationResponseDTO getRecommendationById(Long recoId);
}
