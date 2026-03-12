package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.ModelPortfolioRequestDTO;
import com.wealthpro.goals.dto.ModelPortfolioResponseDTO;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.service.impl.ModelPortfolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelPortfolioServiceImplTest {

    @Mock
    private ModelPortfolioRepository modelPortfolioRepository;

    @Mock
    private ExternalIntegrationService externalIntegrationService;

    @InjectMocks
    private ModelPortfolioServiceImpl modelPortfolioService;

    private ModelPortfolio modelPortfolio;
    private ModelPortfolioRequestDTO modelPortfolioRequestDTO;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void createModelPortfolio_ShouldReturnModelPortfolioResponseDTO() {
        when(externalIntegrationService.validateRiskClassExists("HIGH")).thenReturn(true);
        when(modelPortfolioRepository.save(any(ModelPortfolio.class))).thenReturn(modelPortfolio);

        ModelPortfolioResponseDTO response = modelPortfolioService.createModelPortfolio(modelPortfolioRequestDTO);

        assertNotNull(response);
        assertEquals(modelPortfolio.getName(), response.getName());
        verify(modelPortfolioRepository, times(1)).save(any(ModelPortfolio.class));
    }

    @Test
    void getAllActiveModelPortfolios_ShouldReturnListOfPortfolios() {
        when(modelPortfolioRepository.findByStatus("ACTIVE")).thenReturn(Collections.singletonList(modelPortfolio));

        List<ModelPortfolioResponseDTO> responses = modelPortfolioService.getAllActiveModelPortfolios();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACTIVE", responses.get(0).getStatus());
    }
}
