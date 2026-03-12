package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.RecommendationRequestDTO;
import com.wealthpro.goals.dto.RecommendationResponseDTO;
import com.wealthpro.goals.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@Valid @RequestBody RecommendationRequestDTO recommendationRequestDTO) {
        return new ResponseEntity<>(recommendationService.createRecommendation(recommendationRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendationsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByClientId(clientId));
    }

    @GetMapping("/{recoId}")
    public ResponseEntity<RecommendationResponseDTO> getRecommendationById(@PathVariable Long recoId) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(recoId));
    }
}
