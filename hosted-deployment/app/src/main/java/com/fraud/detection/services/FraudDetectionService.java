package com.fraud.detection.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class FraudDetectionService {

    private static final Logger logger =
            Logger.getLogger(
                    FraudDetectionService.class.getName()
            );

    @Value("${ml.api.url}")
    private String mlApiUrl;

    private final RestTemplate restTemplate;

    public FraudDetectionService(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public String checkFraud(
            Map<String, Object> transactionData
    ) throws Exception {

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        String jsonRequest =
                new ObjectMapper()
                        .writeValueAsString(
                                transactionData
                        );

        HttpEntity<String> entity =
                new HttpEntity<>(
                        jsonRequest,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        mlApiUrl,
                        entity,
                        String.class
                );

        return response.getBody();
    }
}