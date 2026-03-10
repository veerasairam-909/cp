package com.wealthpro.goals.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "model_portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String riskClass;

    @Column(columnDefinition = "json")
    private String weightsJson;

    @Column(nullable = false)
    private String status;
}
