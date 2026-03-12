package com.wealthpro.goals.repository;

import com.wealthpro.goals.entity.Goal;
import com.wealthpro.goals.enums.GoalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void findByClientId_ShouldReturnGoalsForClient() {
        // Arrange
        Goal goal1 = Goal.builder()
                .clientId(100L)
                .goalType(GoalType.RETIREMENT)
                .targetAmount(new BigDecimal("1000000"))
                .targetDate(LocalDate.now().plusYears(15))
                .status("ACTIVE")
                .build();
        Goal goal2 = Goal.builder()
                .clientId(100L)
                .goalType(GoalType.WEALTH)
                .targetAmount(new BigDecimal("500000"))
                .targetDate(LocalDate.now().plusYears(5))
                .status("ACTIVE")
                .build();
        Goal goal3 = Goal.builder()
                .clientId(200L)
                .goalType(GoalType.EDUCATION)
                .targetAmount(new BigDecimal("200000"))
                .targetDate(LocalDate.now().plusYears(10))
                .status("ACTIVE")
                .build();

        goalRepository.save(goal1);
        goalRepository.save(goal2);
        goalRepository.save(goal3);

        // Act
        List<Goal> client100Goals = goalRepository.findByClientId(100L);
        List<Goal> client200Goals = goalRepository.findByClientId(200L);

        // Assert
        assertEquals(2, client100Goals.size());
        assertEquals(1, client200Goals.size());
        assertTrue(client100Goals.stream().anyMatch(g -> g.getGoalType() == GoalType.RETIREMENT));
    }
}
