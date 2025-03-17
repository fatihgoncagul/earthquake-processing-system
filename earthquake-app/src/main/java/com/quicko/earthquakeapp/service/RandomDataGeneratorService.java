package com.quicko.earthquakeapp.service;

import com.quicko.earthquakeapp.model.Earthquake;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class RandomDataGeneratorService {

    private final Random random = new Random();
    private final KafkaTemplate<String, Earthquake> kafkaTemplate;
    private final ThreadPoolTaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;

    public String startGeneratingRandomData() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            return "Data generation is already running!";
        }

        System.out.println("🚀 Random earthquake generation started!");
        scheduledTask = scheduler.scheduleAtFixedRate(
                this::generateRandomData,
                Instant.now().plusSeconds(1),
                Duration.ofSeconds(5)
        );
        return "Random data generation started.";
    }

    public String stopGeneratingRandomData() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            System.out.println("Random earthquake generation stopped!");
        }
        return "Random data generation stopped.";
    }

    private void generateRandomData() {
        Earthquake earthquake = new Earthquake();
        earthquake.setLatitude(random.nextDouble() * 180 - 90);
        earthquake.setLongitude(random.nextDouble() * 360 - 180);
        earthquake.setMagnitude(random.nextDouble() * 10);

        try {
            sendEarthquake(earthquake);
        } catch (Exception e) {
            System.err.println("Error sending earthquake via Kafka: " + e.getMessage());
        }

    }

    public void sendEarthquake(Earthquake earthquake) {
        try {
            kafkaTemplate.send("earthquakes", earthquake);
        } catch (Exception e) {
            System.out.println("Error sending earthquake via Kafka: " + e.getMessage());
        }

    }
}
