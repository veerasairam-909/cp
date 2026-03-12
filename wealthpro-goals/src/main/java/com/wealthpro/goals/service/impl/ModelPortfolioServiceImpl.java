package com.wealthpro.goals.service.impl;

import com.wealthpro.goals.dto.ModelPortfolioDTO;
import com.wealthpro.goals.entity.ModelPortfolio;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.ModelPortfolioRepository;
import com.wealthpro.goals.service.ModelPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelPortfolioServiceImpl implements ModelPortfolioService {

    private final ModelPortfolioRepository modelPortfolioRepository;
    private final ExternalIntegrationService externalIntegrationService;

    @Override
    @Transactional
    public ModelPortfolioDTO createModelPortfolio(ModelPortfolioDTO requestDTO) {
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
        return mapToModelPortfolioDTO(savedModel);
    }

    @Override
    public List<ModelPortfolioDTO> getAllActiveModelPortfolios() {
        return modelPortfolioRepository.findByStatus("ACTIVE").stream()
                .map(this::mapToModelPortfolioDTO)
                .collect(Collectors.toList());
    }

    public ModelPortfolioDTO mapToModelPortfolioDTO(ModelPortfolio model) {
        ModelPortfolioDTO dto = new ModelPortfolioDTO();
        dto.setModelId(model.getModelId());
        dto.setName(model.getName());
        dto.setRiskClass(model.getRiskClass());
        dto.setWeightsJson(model.getWeightsJson());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
