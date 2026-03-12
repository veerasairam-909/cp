package com.wealthpro.goals.service.impl;

import com.wealthpro.goals.dto.ModelPortfolioDTO;
import com.wealthpro.goals.dto.RecommendationDTO;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.entity.Recommendation;
import com.wealthpro.goals.exception.ResourceNotFoundException;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.repository.RecommendationRepository;
import com.wealthpro.goals.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ModelPortfolioRepository modelPortfolioRepository;
    private final com.wealthpro.goals.repository.GoalRepository goalRepository;
    private final ExternalIntegrationService externalIntegrationService;

    @Override
    @Transactional
    public RecommendationDTO createRecommendation(RecommendationDTO requestDTO) {
        if (!externalIntegrationService.validateClientExists(requestDTO.getClientId())) {
            throw new IllegalArgumentException("Invalid Client ID from External Service: " + requestDTO.getClientId());
        }

        ModelPortfolio modelPortfolio = modelPortfolioRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model Portfolio not found with id: " + requestDTO.getModelId()));

        // Calculate total target amount for the client
        java.math.BigDecimal totalGoalAmount = goalRepository.findByClientId(requestDTO.getClientId()).stream()
                .map(com.wealthpro.goals.entity.Goal::getTargetAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        // Generate proposal with split amounts based on weightsJson
        String enrichedProposalJson = requestDTO.getProposalJson();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, Object> baseProposal = new java.util.HashMap<>();
            if (requestDTO.getProposalJson() != null && !requestDTO.getProposalJson().trim().isEmpty()) {
                baseProposal = mapper.readValue(requestDTO.getProposalJson(), new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {});
            }

            java.util.Map<String, Double> weights = new java.util.HashMap<>();
            if (modelPortfolio.getWeightsJson() != null && !modelPortfolio.getWeightsJson().trim().isEmpty()) {
                weights = mapper.readValue(modelPortfolio.getWeightsJson(), new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Double>>() {});
            }

            java.util.Map<String, java.math.BigDecimal> allocations = new java.util.HashMap<>();
            for (java.util.Map.Entry<String, Double> entry : weights.entrySet()) {
                java.math.BigDecimal weightFraction = java.math.BigDecimal.valueOf(entry.getValue()).divide(java.math.BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
                allocations.put(entry.getKey(), totalGoalAmount.multiply(weightFraction).setScale(2, java.math.RoundingMode.HALF_UP));
            }

            baseProposal.put("totalAmount", totalGoalAmount);
            baseProposal.put("allocations", allocations);
            enrichedProposalJson = mapper.writeValueAsString(baseProposal);
        } catch (Exception e) {
            // Fallback to original if parsing fails
            System.err.println("Failed to parse or enrich JSON: " + e.getMessage());
        }

        Recommendation recommendation = Recommendation.builder()
                .clientId(requestDTO.getClientId())
                .modelPortfolio(modelPortfolio)
                .proposalJson(enrichedProposalJson)
                .proposedDate(LocalDateTime.now())
                .status(requestDTO.getStatus())
                .build();
        Recommendation savedRecommendation = recommendationRepository.save(recommendation);
        return mapToRecommendationDTO(savedRecommendation);
    }

    @Override
    public List<RecommendationDTO> getRecommendationsByClientId(Long clientId) {
        return recommendationRepository.findByClientId(clientId).stream()
                .map(this::mapToRecommendationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecommendationDTO getRecommendationById(Long recoId) {
        Recommendation recommendation = recommendationRepository.findById(recoId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found with id: " + recoId));
        return mapToRecommendationDTO(recommendation);
    }

    private RecommendationDTO mapToRecommendationDTO(Recommendation recommendation) {
        RecommendationDTO dto = new RecommendationDTO();
        dto.setRecoId(recommendation.getRecoId());
        dto.setClientId(recommendation.getClientId());
        dto.setModelPortfolio(mapToModelPortfolioDTO(recommendation.getModelPortfolio()));
        dto.setProposalJson(recommendation.getProposalJson());
        dto.setProposedDate(recommendation.getProposedDate());
        dto.setStatus(recommendation.getStatus());
        return dto;
    }

    private ModelPortfolioDTO mapToModelPortfolioDTO(ModelPortfolio model) {
        ModelPortfolioDTO dto = new ModelPortfolioDTO();
        dto.setModelId(model.getModelId());
        dto.setName(model.getName());
        dto.setRiskClass(model.getRiskClass());
        dto.setWeightsJson(model.getWeightsJson());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
