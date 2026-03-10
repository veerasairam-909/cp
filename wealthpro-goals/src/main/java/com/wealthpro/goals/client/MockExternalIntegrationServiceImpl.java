package com.wealthpro.goals.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * MOCK Implementation for the Evaluation demo.
 * Later, you can swap this class with a real FeignClient or RestTemplate call
 * when the external microservices are ready.
 */
@Service
@Slf4j
public class MockExternalIntegrationServiceImpl implements ExternalIntegrationService {

    @Override
    public boolean validateClientExists(Long clientId) {
        log.info("MOCK API CALL: Validating if Client ID [{}] exists in the Client Microservice...", clientId);
        // Simulating a fast API call
        // For the demo, we will accept any Client ID > 0 as valid
        return clientId != null && clientId > 0;
    }

    @Override
    public boolean validateRiskClassExists(String riskClass) {
        log.info("MOCK API CALL: Validating if Risk Class [{}] is valid in the Risk Microservice...", riskClass);
        // Enforcing some basic validation for the demo
        return riskClass != null && !riskClass.isBlank();
    }
}
