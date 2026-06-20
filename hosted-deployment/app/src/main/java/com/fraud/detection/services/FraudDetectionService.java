package com.fraud.detection.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.logging.Logger;

@Service  // Ensure this annotation is present
public class FraudDetectionService {
    private static final String ML_API_URL = "http://localhost:5000/predict";
    private static final Logger logger = Logger.getLogger(FraudDetectionService.class.getName());

    private final RestTemplate restTemplate;

    public FraudDetectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String checkFraud(Map<String, Object> transactionData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonRequest = new ObjectMapper().writeValueAsString(transactionData);
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ML_API_URL, entity, String.class);
        return response.getBody();
    }
}
