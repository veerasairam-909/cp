package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.ModelPortfolioRequestDTO;
import com.wealthpro.goals.dto.ModelPortfolioResponseDTO;

import java.util.List;

public interface ModelPortfolioService {
    ModelPortfolioResponseDTO createModelPortfolio(ModelPortfolioRequestDTO modelPortfolioRequestDTO);
    List<ModelPortfolioResponseDTO> getAllActiveModelPortfolios();
}
