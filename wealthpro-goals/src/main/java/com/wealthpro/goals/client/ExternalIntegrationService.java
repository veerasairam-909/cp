package com.wealthpro.goals.client;

/**
 * Interface that defines the contract for communicating with other external microservices.
 */
public interface ExternalIntegrationService {
    
    /**
     * Calls the Client/Identity Microservice to verify if a client exists.
     */
    boolean validateClientExists(Long clientId);

    /**
     * Calls the Risk Profiling Microservice to verify if a risk class exists and is valid.
     */
    boolean validateRiskClassExists(String riskClass);
}
