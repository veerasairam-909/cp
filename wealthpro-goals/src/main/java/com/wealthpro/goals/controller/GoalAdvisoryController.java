package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.*;
import com.wealthpro.goals.service.GoalAdvisoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/advisory")
@RequiredArgsConstructor
public class GoalAdvisoryController {

    private final GoalAdvisoryService goalAdvisoryService;

    // --- Goals ---
    @PostMapping("/goals")
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {
        return new ResponseEntity<>(goalAdvisoryService.createGoal(goalRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/goals/client/{clientId}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(goalAdvisoryService.getGoalsByClientId(clientId));
    }

    @PatchMapping("/goals/{goalId}/status")
    public ResponseEntity<GoalResponseDTO> updateGoalStatus(@PathVariable Long goalId, @RequestParam String status) {
        return ResponseEntity.ok(goalAdvisoryService.updateGoalStatus(goalId, status));
    }

    // --- Model Portfolios ---
    @PostMapping("/models")
    public ResponseEntity<ModelPortfolioResponseDTO> createModelPortfolio(@Valid @RequestBody ModelPortfolioRequestDTO modelPortfolioRequestDTO) {
        return new ResponseEntity<>(goalAdvisoryService.createModelPortfolio(modelPortfolioRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/models/active")
    public ResponseEntity<List<ModelPortfolioResponseDTO>> getAllActiveModelPortfolios() {
        return ResponseEntity.ok(goalAdvisoryService.getAllActiveModelPortfolios());
    }

    // --- Recommendations ---
    @PostMapping("/recommendations")
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@Valid @RequestBody RecommendationRequestDTO recommendationRequestDTO) {
        return new ResponseEntity<>(goalAdvisoryService.createRecommendation(recommendationRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/recommendations/client/{clientId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendationsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(goalAdvisoryService.getRecommendationsByClientId(clientId));
    }

    @GetMapping("/recommendations/{recoId}")
    public ResponseEntity<RecommendationResponseDTO> getRecommendationById(@PathVariable Long recoId) {
        return ResponseEntity.ok(goalAdvisoryService.getRecommendationById(recoId));
    }
}
