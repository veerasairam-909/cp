package com.wealthpro.goals.controller;

import com.wealthpro.goals.dto.GoalDTO;
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
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goalDTO) {
        return new ResponseEntity<>(goalService.createGoal(goalDTO), HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(goalService.getGoalsByClientId(clientId));
    }

    @PatchMapping("/{goalId}/status")
    public ResponseEntity<GoalDTO> updateGoalStatus(@PathVariable Long goalId, @RequestParam String status) {
        return ResponseEntity.ok(goalService.updateGoalStatus(goalId, status));
    }
}
