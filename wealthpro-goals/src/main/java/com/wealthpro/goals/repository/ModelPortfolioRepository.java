package com.wealthpro.goals.repository;

import com.wealthpro.goals.entity.ModelPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelPortfolioRepository extends JpaRepository<ModelPortfolio, Long> {
    List<ModelPortfolio> findByStatus(String status);
}
