package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.ModelPortfolioRequestDTO;
import com.wealthpro.goals.dto.ModelPortfolioResponseDTO;
import com.wealthpro.goals.service.ModelPortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
public class ModelPortfolioController {

    private final ModelPortfolioService modelPortfolioService;

    @PostMapping
    public ResponseEntity<ModelPortfolioResponseDTO> createModelPortfolio(@Valid @RequestBody ModelPortfolioRequestDTO modelPortfolioRequestDTO) {
        return new ResponseEntity<>(modelPortfolioService.createModelPortfolio(modelPortfolioRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ModelPortfolioResponseDTO>> getAllActiveModelPortfolios() {
        return ResponseEntity.ok(modelPortfolioService.getAllActiveModelPortfolios());
    }
}
