package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.GoalRequestDTO;
import com.wealthpro.goals.dto.GoalResponseDTO;
import com.wealthpro.goals.entity.Goal;
import com.wealthpro.goals.enums.GoalType;
import com.wealthpro.goals.exception.ResourceNotFoundException;
import com.wealthpro.goals.client.ExternalIntegrationService;
import com.wealthpro.goals.repository.GoalRepository;
import com.wealthpro.goals.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ExternalIntegrationService externalIntegrationService;

    @InjectMocks
    private GoalServiceImpl goalService;

    private Goal goal;
    private GoalRequestDTO goalRequestDTO;

    @BeforeEach
    void setUp() {
        goal = Goal.builder()
                .goalId(1L)
                .clientId(100L)
                .goalType(GoalType.RETIREMENT)
                .targetAmount(new BigDecimal("1000000"))
                .targetDate(LocalDate.now().plusYears(10))
                .priority(1)
                .status("ACTIVE")
                .build();

        goalRequestDTO = new GoalRequestDTO();
        goalRequestDTO.setClientId(100L);
        goalRequestDTO.setGoalType(GoalType.RETIREMENT);
        goalRequestDTO.setTargetAmount(new BigDecimal("1000000"));
        goalRequestDTO.setTargetDate(LocalDate.now().plusYears(10));
        goalRequestDTO.setPriority(1);
        goalRequestDTO.setStatus("ACTIVE");
    }

    @Test
    void createGoal_ShouldReturnGoalResponseDTO() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponseDTO response = goalService.createGoal(goalRequestDTO);

        assertNotNull(response);
        assertEquals(goal.getGoalId(), response.getGoalId());
        assertEquals(goal.getClientId(), response.getClientId());
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void getGoalsByClientId_ShouldReturnListOfGoals() {
        when(goalRepository.findByClientId(100L)).thenReturn(Collections.singletonList(goal));

        List<GoalResponseDTO> responses = goalService.getGoalsByClientId(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(goal.getGoalId(), responses.get(0).getGoalId());
    }

    @Test
    void updateGoalStatus_ShouldReturnUpdatedGoal() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponseDTO response = goalService.updateGoalStatus(1L, "COMPLETED");

        assertNotNull(response);
        assertEquals("COMPLETED", goal.getStatus());
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void updateGoalStatus_ShouldThrowExceptionWhenNotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> goalService.updateGoalStatus(1L, "COMPLETED"));
    }
}
