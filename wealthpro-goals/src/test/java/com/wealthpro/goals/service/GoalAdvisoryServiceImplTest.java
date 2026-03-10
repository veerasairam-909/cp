package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.*;
import com.wealthpro.goals.entity.Goal;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.entity.Recommendation;
import com.wealthpro.goals.exception.ResourceNotFoundException;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.GoalRepository;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.repository.RecommendationRepository;
import com.wealthpro.goals.service.impl.GoalAdvisoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalAdvisoryServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ModelPortfolioRepository modelPortfolioRepository;

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private ExternalIntegrationService externalIntegrationService;

    @InjectMocks
    private GoalAdvisoryServiceImpl goalAdvisoryService;

    private Goal goal;
    private GoalRequestDTO goalRequestDTO;

    private ModelPortfolio modelPortfolio;
    private ModelPortfolioRequestDTO modelPortfolioRequestDTO;

    private Recommendation recommendation;
    private RecommendationRequestDTO recommendationRequestDTO;

    @BeforeEach
    void setUp() {
        goal = Goal.builder()
                .goalId(1L)
                .clientId(100L)
                .goalType(Goal.GoalType.RETIREMENT)
                .targetAmount(new BigDecimal("1000000"))
                .targetDate(LocalDate.now().plusYears(10))
                .priority(1)
                .status("ACTIVE")
                .build();

        goalRequestDTO = new GoalRequestDTO();
        goalRequestDTO.setClientId(100L);
        goalRequestDTO.setGoalType(Goal.GoalType.RETIREMENT);
        goalRequestDTO.setTargetAmount(new BigDecimal("1000000"));
        goalRequestDTO.setTargetDate(LocalDate.now().plusYears(10));
        goalRequestDTO.setPriority(1);
        goalRequestDTO.setStatus("ACTIVE");

        modelPortfolio = ModelPortfolio.builder()
                .modelId(1L)
                .name("Aggressive Growth")
                .riskClass("HIGH")
                .weightsJson("{\"equity\":80,\"debt\":20}")
                .status("ACTIVE")
                .build();

        modelPortfolioRequestDTO = new ModelPortfolioRequestDTO();
        modelPortfolioRequestDTO.setName("Aggressive Growth");
        modelPortfolioRequestDTO.setRiskClass("HIGH");
        modelPortfolioRequestDTO.setWeightsJson("{\"equity\":80,\"debt\":20}");
        modelPortfolioRequestDTO.setStatus("ACTIVE");

        recommendation = Recommendation.builder()
                .recoId(1L)
                .clientId(100L)
                .modelPortfolio(modelPortfolio)
                .proposalJson("{\"comments\":\"Recommended based on risk profile\"}")
                .proposedDate(LocalDateTime.now())
                .status(Recommendation.RecommendationStatus.DRAFT)
                .build();

        recommendationRequestDTO = new RecommendationRequestDTO();
        recommendationRequestDTO.setClientId(100L);
        recommendationRequestDTO.setModelId(1L);
        recommendationRequestDTO.setProposalJson("{\"comments\":\"Recommended based on risk profile\"}");
        recommendationRequestDTO.setStatus(Recommendation.RecommendationStatus.DRAFT);
    }

    // --- Goal Tests ---
    @Test
    void createGoal_ShouldReturnGoalResponseDTO() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponseDTO response = goalAdvisoryService.createGoal(goalRequestDTO);

        assertNotNull(response);
        assertEquals(goal.getGoalId(), response.getGoalId());
        assertEquals(goal.getClientId(), response.getClientId());
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void getGoalsByClientId_ShouldReturnListOfGoals() {
        when(goalRepository.findByClientId(100L)).thenReturn(Collections.singletonList(goal));

        List<GoalResponseDTO> responses = goalAdvisoryService.getGoalsByClientId(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(goal.getGoalId(), responses.get(0).getGoalId());
    }

    @Test
    void updateGoalStatus_ShouldReturnUpdatedGoal() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponseDTO response = goalAdvisoryService.updateGoalStatus(1L, "COMPLETED");

        assertNotNull(response);
        assertEquals("COMPLETED", goal.getStatus());
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void updateGoalStatus_ShouldThrowExceptionWhenNotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalAdvisoryService.updateGoalStatus(1L, "COMPLETED"));
    }

    // --- Model Portfolio Tests ---
    @Test
    void createModelPortfolio_ShouldReturnModelPortfolioResponseDTO() {
        when(externalIntegrationService.validateRiskClassExists("HIGH")).thenReturn(true);
        when(modelPortfolioRepository.save(any(ModelPortfolio.class))).thenReturn(modelPortfolio);

        ModelPortfolioResponseDTO response = goalAdvisoryService.createModelPortfolio(modelPortfolioRequestDTO);

        assertNotNull(response);
        assertEquals(modelPortfolio.getName(), response.getName());
        verify(modelPortfolioRepository, times(1)).save(any(ModelPortfolio.class));
    }

    @Test
    void getAllActiveModelPortfolios_ShouldReturnListOfPortfolios() {
        when(modelPortfolioRepository.findByStatus("ACTIVE")).thenReturn(Collections.singletonList(modelPortfolio));

        List<ModelPortfolioResponseDTO> responses = goalAdvisoryService.getAllActiveModelPortfolios();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACTIVE", responses.get(0).getStatus());
    }

    // --- Recommendation Tests ---
    @Test
    void createRecommendation_ShouldReturnRecommendationResponseDTO() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(modelPortfolioRepository.findById(1L)).thenReturn(Optional.of(modelPortfolio));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);

        RecommendationResponseDTO response = goalAdvisoryService.createRecommendation(recommendationRequestDTO);

        assertNotNull(response);
        assertEquals(recommendation.getRecoId(), response.getRecoId());
        assertEquals(modelPortfolio.getName(), response.getModelPortfolio().getName());
        verify(recommendationRepository, times(1)).save(any(Recommendation.class));
    }

    @Test
    void createRecommendation_ShouldThrowExceptionWhenModelNotFound() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(modelPortfolioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalAdvisoryService.createRecommendation(recommendationRequestDTO));
    }

    @Test
    void getRecommendationsByClientId_ShouldReturnList() {
        when(recommendationRepository.findByClientId(100L)).thenReturn(Collections.singletonList(recommendation));

        List<RecommendationResponseDTO> responses = goalAdvisoryService.getRecommendationsByClientId(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(recommendation.getClientId(), responses.get(0).getClientId());
    }

    @Test
    void getRecommendationById_ShouldReturnRecommendation() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));

        RecommendationResponseDTO response = goalAdvisoryService.getRecommendationById(1L);

        assertNotNull(response);
        assertEquals(recommendation.getRecoId(), response.getRecoId());
    }

    @Test
    void getRecommendationById_ShouldThrowExceptionWhenNotFound() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalAdvisoryService.getRecommendationById(1L));
    }
}
