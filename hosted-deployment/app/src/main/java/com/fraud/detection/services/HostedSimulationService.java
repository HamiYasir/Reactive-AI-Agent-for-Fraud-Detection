package com.fraud.detection.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Service
public class HostedSimulationService {

    private final FraudDetectionService fraudDetectionService;
    private final SimpMessagingTemplate messagingTemplate;

    private volatile boolean running = false;
    private volatile boolean stopRequested = false;

    public HostedSimulationService(
            FraudDetectionService fraudDetectionService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.fraudDetectionService = fraudDetectionService;
        this.messagingTemplate = messagingTemplate;
    }

    public synchronized void start() {

        if (running) {

            System.out.println(
                    "Hosted simulation already running."
            );

            return;
        }

        running = true;
        stopRequested = false;

        new Thread(() -> {

            try {

                ClassPathResource resource =
                        new ClassPathResource(
                                "data/creditcard_test.csv"
                        );

                Reader reader =
                        new InputStreamReader(
                                resource.getInputStream()
                        );

                CSVParser parser =
                        CSVFormat.DEFAULT
                                .builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .build()
                                .parse(reader);

                ObjectMapper mapper =
                        new ObjectMapper();

                for (CSVRecord record : parser) {

                    if (stopRequested) {

                        System.out.println(
                                "Hosted simulation stop requested."
                        );

                        break;
                    }

                    try {

                        Map<String, Object> transaction =
                                new HashMap<>();

                        for (String header :
                                parser.getHeaderNames()) {

                            if (
                                    header.equalsIgnoreCase("id")
                                            ||
                                            header.equalsIgnoreCase("Class")
                            ) {
                                continue;
                            }

                            transaction.put(
                                    header,
                                    Double.parseDouble(
                                            record.get(header)
                                    )
                            );
                        }

                        String predictionJson =
                                fraudDetectionService
                                        .checkFraud(
                                                transaction
                                        );

                        Object prediction =
                                mapper.readValue(
                                        predictionJson,
                                        new TypeReference<Object>() {}
                                );

                        Map<String, Object> payload =
                                new HashMap<>();

                        payload.put(
                                "transaction",
                                transaction
                        );

                        payload.put(
                                "prediction",
                                prediction
                        );

                        messagingTemplate.convertAndSend(
                                "/topic/transactions",
                                payload
                        );

                        Thread.sleep(1000);

                    } catch (Exception rowException) {

                        rowException.printStackTrace();
                    }
                }

                parser.close();

                System.out.println(
                        "Hosted simulation completed."
                );

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                running = false;

                System.out.println(
                        "Hosted simulation stopped."
                );
            }

        }).start();
    }

    public synchronized void stop() {

        if (!running) {

            System.out.println(
                    "No hosted simulation running."
            );

            return;
        }

        stopRequested = true;

        System.out.println(
                "Hosted simulation stopping..."
        );
    }

    public boolean isRunning() {

        return running;
    }
}