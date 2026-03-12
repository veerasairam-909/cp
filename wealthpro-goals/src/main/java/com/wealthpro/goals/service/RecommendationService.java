package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.RecommendationDTO;

import java.util.List;

public interface RecommendationService {
    RecommendationDTO createRecommendation(RecommendationDTO recommendationDTO);
    List<RecommendationDTO> getRecommendationsByClientId(Long clientId);
    RecommendationDTO getRecommendationById(Long recoId);
}
