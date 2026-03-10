package com.wealthpro.goals.service.impl;

import com.wealthpro.goals.dto.*;
import com.wealthpro.goals.entity.Goal;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.entity.Recommendation;
import com.wealthpro.goals.exception.ResourceNotFoundException;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.GoalRepository;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.repository.RecommendationRepository;
import com.wealthpro.goals.service.GoalAdvisoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalAdvisoryServiceImpl implements GoalAdvisoryService {

    private final GoalRepository goalRepository;
    private final ModelPortfolioRepository modelPortfolioRepository;
    private final RecommendationRepository recommendationRepository;
    private final ExternalIntegrationService externalIntegrationService;

    @Override
    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO requestDTO) {
        // Validation using external microservice mock
        if (!externalIntegrationService.validateClientExists(requestDTO.getClientId())) {
            throw new IllegalArgumentException("Invalid Client ID from External Service: " + requestDTO.getClientId());
        }

        Goal goal = Goal.builder()
                .clientId(requestDTO.getClientId())
                .goalType(requestDTO.getGoalType())
                .targetAmount(requestDTO.getTargetAmount())
                .targetDate(requestDTO.getTargetDate())
                .priority(requestDTO.getPriority())
                .status(requestDTO.getStatus())
                .build();
        Goal savedGoal = goalRepository.save(goal);
        return mapToGoalResponseDTO(savedGoal);
    }

    @Override
    public List<GoalResponseDTO> getGoalsByClientId(Long clientId) {
        return goalRepository.findByClientId(clientId).stream()
                .map(this::mapToGoalResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoalStatus(Long goalId, String status) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));
        goal.setStatus(status);
        Goal updatedGoal = goalRepository.save(goal);
        return mapToGoalResponseDTO(updatedGoal);
    }

    @Override
    @Transactional
    public ModelPortfolioResponseDTO createModelPortfolio(ModelPortfolioRequestDTO requestDTO) {
        // Validation using external microservice mock
        if (!externalIntegrationService.validateRiskClassExists(requestDTO.getRiskClass())) {
            throw new IllegalArgumentException("Invalid Risk Class from External Service: " + requestDTO.getRiskClass());
        }

        ModelPortfolio modelPortfolio = ModelPortfolio.builder()
                .name(requestDTO.getName())
                .riskClass(requestDTO.getRiskClass())
                .weightsJson(requestDTO.getWeightsJson())
                .status(requestDTO.getStatus())
                .build();
        ModelPortfolio savedModel = modelPortfolioRepository.save(modelPortfolio);
        return mapToModelPortfolioResponseDTO(savedModel);
    }

    @Override
    public List<ModelPortfolioResponseDTO> getAllActiveModelPortfolios() {
        return modelPortfolioRepository.findByStatus("ACTIVE").stream()
                .map(this::mapToModelPortfolioResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecommendationResponseDTO createRecommendation(RecommendationRequestDTO requestDTO) {
        // Validate client
        if (!externalIntegrationService.validateClientExists(requestDTO.getClientId())) {
            throw new IllegalArgumentException("Invalid Client ID from External Service: " + requestDTO.getClientId());
        }

        ModelPortfolio modelPortfolio = modelPortfolioRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model Portfolio not found with id: " + requestDTO.getModelId()));

        Recommendation recommendation = Recommendation.builder()
                .clientId(requestDTO.getClientId())
                .modelPortfolio(modelPortfolio)
                .proposalJson(requestDTO.getProposalJson())
                .proposedDate(LocalDateTime.now())
                .status(requestDTO.getStatus())
                .build();
        Recommendation savedRecommendation = recommendationRepository.save(recommendation);
        return mapToRecommendationResponseDTO(savedRecommendation);
    }

    @Override
    public List<RecommendationResponseDTO> getRecommendationsByClientId(Long clientId) {
        return recommendationRepository.findByClientId(clientId).stream()
                .map(this::mapToRecommendationResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponseDTO getRecommendationById(Long recoId) {
        Recommendation recommendation = recommendationRepository.findById(recoId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found with id: " + recoId));
        return mapToRecommendationResponseDTO(recommendation);
    }

    // Mapping helper methods
    private GoalResponseDTO mapToGoalResponseDTO(Goal goal) {
        GoalResponseDTO dto = new GoalResponseDTO();
        dto.setGoalId(goal.getGoalId());
        dto.setClientId(goal.getClientId());
        dto.setGoalType(goal.getGoalType());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setTargetDate(goal.getTargetDate());
        dto.setPriority(goal.getPriority());
        dto.setStatus(goal.getStatus());
        return dto;
    }

    private ModelPortfolioResponseDTO mapToModelPortfolioResponseDTO(ModelPortfolio model) {
        ModelPortfolioResponseDTO dto = new ModelPortfolioResponseDTO();
        dto.setModelId(model.getModelId());
        dto.setName(model.getName());
        dto.setRiskClass(model.getRiskClass());
        dto.setWeightsJson(model.getWeightsJson());
        dto.setStatus(model.getStatus());
        return dto;
    }

    private RecommendationResponseDTO mapToRecommendationResponseDTO(Recommendation recommendation) {
        RecommendationResponseDTO dto = new RecommendationResponseDTO();
        dto.setRecoId(recommendation.getRecoId());
        dto.setClientId(recommendation.getClientId());
        dto.setModelPortfolio(mapToModelPortfolioResponseDTO(recommendation.getModelPortfolio()));
        dto.setProposalJson(recommendation.getProposalJson());
        dto.setProposedDate(recommendation.getProposedDate());
        dto.setStatus(recommendation.getStatus());
        return dto;
    }
}
