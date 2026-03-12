package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.ModelPortfolioDTO;
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
    public ResponseEntity<ModelPortfolioDTO> createModelPortfolio(@Valid @RequestBody ModelPortfolioDTO modelPortfolioDTO) {
        return new ResponseEntity<>(modelPortfolioService.createModelPortfolio(modelPortfolioDTO), HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ModelPortfolioDTO>> getAllActiveModelPortfolios() {
        return ResponseEntity.ok(modelPortfolioService.getAllActiveModelPortfolios());
    }
}
