package com.wealthpro.goals.service.impl;

import com.wealthpro.goals.dto.GoalRequestDTO;
import com.wealthpro.goals.dto.GoalResponseDTO;
import com.wealthpro.goals.entity.Goal;
import com.wealthpro.goals.exception.ResourceNotFoundException;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.GoalRepository;
import com.wealthpro.goals.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final ExternalIntegrationService externalIntegrationService;

    @Override
    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO requestDTO) {
        if (!externalIntegrationService.validateClientExists(requestDTO.getClientId())) {
            throw new IllegalArgumentException("Invalid Client ID from External Service: " + requestDTO.getClientId());
        }

        Goal goal = Goal.builder()
                .clientId(requestDTO.getClientId())
                .goalType(requestDTO.getGoalType())
                .targetAmount(requestDTO.getTargetAmount())
                .targetDate(requestDTO.getTargetDate())
                .priority(requestDTO.getPriority())
                .status(requestDTO.getStatus())
                .build();
        Goal savedGoal = goalRepository.save(goal);
        return mapToGoalResponseDTO(savedGoal);
    }

    @Override
    public List<GoalResponseDTO> getGoalsByClientId(Long clientId) {
        return goalRepository.findByClientId(clientId).stream()
                .map(this::mapToGoalResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoalStatus(Long goalId, String status) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));
        goal.setStatus(status);
        Goal updatedGoal = goalRepository.save(goal);
        return mapToGoalResponseDTO(updatedGoal);
    }

    private GoalResponseDTO mapToGoalResponseDTO(Goal goal) {
        GoalResponseDTO dto = new GoalResponseDTO();
        dto.setGoalId(goal.getGoalId());
        dto.setClientId(goal.getClientId());
        dto.setGoalType(goal.getGoalType());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setTargetDate(goal.getTargetDate());
        dto.setPriority(goal.getPriority());
        dto.setStatus(goal.getStatus());
        return dto;
    }
}
