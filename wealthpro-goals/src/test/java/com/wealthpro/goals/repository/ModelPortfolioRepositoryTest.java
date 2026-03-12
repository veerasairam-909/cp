package com.wealthpro.goals.repository;

import com.wealthpro.goals.entity.ModelPortfolio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ModelPortfolioRepositoryTest {

    @Autowired
    private ModelPortfolioRepository modelPortfolioRepository;

    @Test
    void findByStatus_ShouldReturnModelPortfoliosWithGivenStatus() {
        // Arrange
        ModelPortfolio activeMp1 = ModelPortfolio.builder()
                .name("Aggressive Growth")
                .riskClass("HIGH")
                .weightsJson("{\"Equity\":80,\"Debt\":20}")
                .status("ACTIVE")
                .build();
        ModelPortfolio activeMp2 = ModelPortfolio.builder()
                .name("Balanced")
                .riskClass("MODERATE")
                .weightsJson("{\"Equity\":60,\"Debt\":40}")
                .status("ACTIVE")
                .build();
        ModelPortfolio inactiveMp = ModelPortfolio.builder()
                .name("Legacy Conservative")
                .riskClass("LOW")
                .weightsJson("{\"Equity\":20,\"Debt\":80}")
                .status("INACTIVE")
                .build();

        modelPortfolioRepository.save(activeMp1);
        modelPortfolioRepository.save(activeMp2);
        modelPortfolioRepository.save(inactiveMp);

        // Act
        List<ModelPortfolio> activePortfolios = modelPortfolioRepository.findByStatus("ACTIVE");
        List<ModelPortfolio> inactivePortfolios = modelPortfolioRepository.findByStatus("INACTIVE");

        // Assert
        assertEquals(2, activePortfolios.size());
        assertEquals(1, inactivePortfolios.size());
        assertTrue(activePortfolios.stream().anyMatch(p -> p.getName().equals("Aggressive Growth")));
        assertEquals("INACTIVE", inactivePortfolios.get(0).getStatus());
    }
}
