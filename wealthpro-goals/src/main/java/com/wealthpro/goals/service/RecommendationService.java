package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.RecommendationRequestDTO;
import com.wealthpro.goals.dto.RecommendationResponseDTO;

import java.util.List;

public interface RecommendationService {
    RecommendationResponseDTO createRecommendation(RecommendationRequestDTO recommendationRequestDTO);
    List<RecommendationResponseDTO> getRecommendationsByClientId(Long clientId);
    RecommendationResponseDTO getRecommendationById(Long recoId);
}
