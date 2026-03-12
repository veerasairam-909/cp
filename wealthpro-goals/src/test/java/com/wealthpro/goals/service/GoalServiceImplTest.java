package com.wealthpro.goals.service;

import com.wealthpro.goals.dto.GoalDTO;
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
    private GoalDTO goalDTO;

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

        goalDTO = new GoalDTO();
        goalDTO.setClientId(100L);
        goalDTO.setGoalType(GoalType.RETIREMENT);
        goalDTO.setTargetAmount(new BigDecimal("1000000"));
        goalDTO.setTargetDate(LocalDate.now().plusYears(10));
        goalDTO.setPriority(1);
        goalDTO.setStatus("ACTIVE");
    }

    @Test
    void createGoal_ShouldReturnGoalDTO() {
        when(externalIntegrationService.validateClientExists(100L)).thenReturn(true);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalDTO response = goalService.createGoal(goalDTO);

        assertNotNull(response);
        assertEquals(goal.getGoalId(), response.getGoalId());
        assertEquals(goal.getClientId(), response.getClientId());
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void getGoalsByClientId_ShouldReturnListOfGoals() {
        when(goalRepository.findByClientId(100L)).thenReturn(Collections.singletonList(goal));

        List<GoalDTO> responses = goalService.getGoalsByClientId(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(goal.getGoalId(), responses.get(0).getGoalId());
    }

    @Test
    void updateGoalStatus_ShouldReturnUpdatedGoal() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalDTO response = goalService.updateGoalStatus(1L, "COMPLETED");

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
