package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.RecommendationDTO;
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
    public ResponseEntity<RecommendationDTO> createRecommendation(@Valid @RequestBody RecommendationDTO recommendationDTO) {
        return new ResponseEntity<>(recommendationService.createRecommendation(recommendationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RecommendationDTO>> getRecommendationsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByClientId(clientId));
    }

    @GetMapping("/{recoId}")
    public ResponseEntity<RecommendationDTO> getRecommendationById(@PathVariable Long recoId) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(recoId));
    }
}
