package com.fraud.detection.controllers;

import com.fraud.detection.services.HostedSimulationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FraudDetectionController {
    private final SimpMessagingTemplate messagingTemplate;
    private final HostedSimulationService hostedSimulationService;

    private Process consumerProcess = null;
    private Process producerProcess = null;


    public FraudDetectionController(
            SimpMessagingTemplate messagingTemplate,
            HostedSimulationService hostedSimulationService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.hostedSimulationService = hostedSimulationService;
    }

    @GetMapping("/mode")
    public ResponseEntity<String> getMode() {

        return ResponseEntity.ok("hosted");
    }

    @PostMapping("/update")
    public void sendUpdateToFrontend(
            @RequestBody Map<String, Object> predictionData
    ) {
        messagingTemplate.convertAndSend(
                "/topic/transactions",
                predictionData
        );
    }

    @PostMapping("/start-consumer")
    public ResponseEntity<String> startConsumer() {

        try {

            if (consumerProcess != null &&
                    consumerProcess.isAlive()) {

                return ResponseEntity.ok(
                        "Consumer already running"
                );
            }

//            ProcessBuilder pb = new ProcessBuilder(
//                    "python",
////                    "D:\\VIT Projects\\AI, ML, Java Digital Assignment\\fraud-detection\\data-streaming\\streaming_data_into_model_from_consumer\\stream_to_consumer.py"
//                    "../../../../../../../data-streaming/streaming_data_into_model_from_consumer/stream_to_consumer.py"
//            );

            Path consumerScript = Paths.get(
                    System.getProperty("user.dir"),
                    "..",
                    "data-streaming",
                    "streaming_data_into_model_from_consumer",
                    "stream_to_consumer.py"
            ).normalize();

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    consumerScript.toString()
            );

            pb.redirectErrorStream(true);
            pb.inheritIO();

            consumerProcess = pb.start();

            System.out.println(
                    "Consumer PID: " +
                            consumerProcess.pid()
            );

            Thread.sleep(3000);

            return ResponseEntity.ok(
                    "Consumer Started"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/start-hosted-demo")
    public ResponseEntity<String> startHostedDemo() {

        hostedSimulationService.start();

        return ResponseEntity.ok(
                "Hosted demo started"
        );
    }

    @PostMapping("/stop-hosted-demo")
    public ResponseEntity<String> stopHostedDemo() {

        hostedSimulationService.stop();

        return ResponseEntity.ok(
                "Hosted demo stopping"
        );
    }

    @PostMapping("/start-simulation")
    public ResponseEntity<String> startSimulation() {

        try {

            if (producerProcess != null &&
                    producerProcess.isAlive()) {

                return ResponseEntity.ok(
                        "Simulation already running"
                );
            }

//            ProcessBuilder pb = new ProcessBuilder(
//                    "python",
////                    "D:\\VIT Projects\\AI, ML, Java Digital Assignment\\fraud-detection\\data-streaming\\streaming_data_from_csv_file _into_producer\\stream_to_producer.py"
//                    "../../../../../../../data-streaming/streaming_data_from_csv_file _into_producer/stream_to_producer.py"
//            );

            Path producerScript = Paths.get(
                    System.getProperty("user.dir"),
                    "..",
                    "data-streaming",
                    "streaming_data_from_csv_file _into_producer",
                    "stream_to_producer.py"
            ).normalize();

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    producerScript.toString()
            );

            pb.redirectErrorStream(true);
            pb.inheritIO();

            producerProcess = pb.start();

            System.out.println(
                    "Producer PID: " +
                            producerProcess.pid()
            );

            return ResponseEntity.ok(
                    "Simulation Started"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/stop-stream")
    public ResponseEntity<String> stopStream() {

        try {

            if (producerProcess != null &&
                    producerProcess.isAlive()) {

                producerProcess.destroyForcibly();

                System.out.println(
                        "Producer stopped"
                );
            }

            if (consumerProcess != null &&
                    consumerProcess.isAlive()) {

                consumerProcess.destroyForcibly();

                System.out.println(
                        "Consumer stopped"
                );
            }

            producerProcess = null;
            consumerProcess = null;

            return ResponseEntity.ok(
                    "Stream stopped"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}
