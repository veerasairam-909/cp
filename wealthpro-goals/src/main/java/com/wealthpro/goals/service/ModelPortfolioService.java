package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.ModelPortfolioDTO;

import java.util.List;

public interface ModelPortfolioService {
    ModelPortfolioDTO createModelPortfolio(ModelPortfolioDTO modelPortfolioDTO);
    List<ModelPortfolioDTO> getAllActiveModelPortfolios();
}
