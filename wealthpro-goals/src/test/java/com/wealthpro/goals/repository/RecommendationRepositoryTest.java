package com.wealthpro.goals.repository;

import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.entity.Recommendation;
import com.wealthpro.goals.enums.RecommendationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecommendationRepositoryTest {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ModelPortfolioRepository modelPortfolioRepository;

    @Test
    void findByClientId_ShouldReturnRecommendationsForClient() {
        // Arrange
        ModelPortfolio mp = ModelPortfolio.builder()
                .name("Aggressive Growth")
                .riskClass("HIGH")
                .weightsJson("{\"Equity\":80,\"Debt\":20}")
                .status("ACTIVE")
                .build();
        mp = modelPortfolioRepository.save(mp);

        Recommendation rec1 = Recommendation.builder()
                .clientId(100L)
                .modelPortfolio(mp)
                .proposalJson("{\"comments\":\"High risk recommendation\"}")
                .proposedDate(LocalDateTime.now())
                .status(RecommendationStatus.DRAFT)
                .build();
        Recommendation rec2 = Recommendation.builder()
                .clientId(200L)
                .modelPortfolio(mp)
                .proposalJson("{\"comments\":\"Alternative recommendation\"}")
                .proposedDate(LocalDateTime.now())
                .status(RecommendationStatus.SUBMITTED)
                .build();

        recommendationRepository.save(rec1);
        recommendationRepository.save(rec2);

        // Act
        List<Recommendation> client100Recs = recommendationRepository.findByClientId(100L);
        List<Recommendation> client200Recs = recommendationRepository.findByClientId(200L);

        // Assert
        assertEquals(1, client100Recs.size());
        assertEquals(1, client200Recs.size());
        assertEquals(100L, client100Recs.get(0).getClientId());
    }
}
