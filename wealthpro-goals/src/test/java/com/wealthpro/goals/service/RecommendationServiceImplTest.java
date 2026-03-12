package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.RecommendationDTO;
import com.wealthpro.goals.enums.RecommendationStatus;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.entity.Recommendation;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.repository.RecommendationRepository;
import com.wealthpro.goals.service.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private ModelPortfolioRepository modelPortfolioRepository;

    @Mock
    private com.wealthpro.goals.repository.GoalRepository goalRepository;

    @Mock
    private ExternalIntegrationService externalIntegrationService;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private ModelPortfolio modelPortfolio;
    private Recommendation recommendation;
    private RecommendationDTO recommendationDTO;

    @BeforeEach
    void setUp() {
        modelPortfolio = ModelPortfolio.builder()
                .modelId(1L)
                .name("Aggressive Growth")
                .riskClass("HIGH")
                .weightsJson("{\"Equity\":80,\"Debt\":20}")
                .status("ACTIVE")
                .build();

        recommendation = Recommendation.builder()
                .recoId(1L)
                .clientId(100L)
                .modelPortfolio(modelPortfolio)
                .proposalJson("{\"comments\":\"Recommended\"}")
                .proposedDate(LocalDateTime.now())
                .status(RecommendationStatus.DRAFT)
                .build();

        recommendationDTO = new RecommendationDTO();
        recommendationDTO.setClientId(100L);
        recommendationDTO.setModelId(1L);
        recommendationDTO.setProposalJson("{\"comments\":\"Recommended\"}");
        recommendationDTO.setStatus(RecommendationStatus.DRAFT);
    }

    @Test
    void createRecommendation_ShouldReturnRecommendationDTO() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(modelPortfolioRepository.findById(1L)).thenReturn(Optional.of(modelPortfolio));
        
        com.wealthpro.goals.entity.Goal testGoal = com.wealthpro.goals.entity.Goal.builder().targetAmount(new java.math.BigDecimal("10000")).build();
        when(goalRepository.findByClientId(100L)).thenReturn(Collections.singletonList(testGoal));
        
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);

        RecommendationDTO response = recommendationService.createRecommendation(recommendationDTO);

        assertNotNull(response);
        assertEquals(recommendation.getRecoId(), response.getRecoId());
        verify(recommendationRepository, times(1)).save(any(Recommendation.class));
    }

    @Test
    void getRecommendationsByClientId_ShouldReturnList() {
        when(recommendationRepository.findByClientId(100L)).thenReturn(Collections.singletonList(recommendation));

        List<RecommendationDTO> responses = recommendationService.getRecommendationsByClientId(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void getRecommendationById_ShouldReturnRecommendation() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));

        RecommendationDTO response = recommendationService.getRecommendationById(1L);

        assertNotNull(response);
        assertEquals(recommendation.getRecoId(), response.getRecoId());
    }
}
