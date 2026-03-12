package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.GoalRequestDTO;
import com.wealthpro.goals.dto.GoalResponseDTO;
import com.wealthpro.goals.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {
        return new ResponseEntity<>(goalService.createGoal(goalRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(goalService.getGoalsByClientId(clientId));
    }

    @PatchMapping("/{goalId}/status")
    public ResponseEntity<GoalResponseDTO> updateGoalStatus(@PathVariable Long goalId, @RequestParam String status) {
        return ResponseEntity.ok(goalService.updateGoalStatus(goalId, status));
    }
}
