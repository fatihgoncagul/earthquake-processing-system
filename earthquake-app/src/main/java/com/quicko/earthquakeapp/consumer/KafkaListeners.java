
package com.quicko.earthquakeapp.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicko.earthquakeapp.model.Earthquake;
import com.quicko.earthquakeapp.service.EarthquakeService;
import com.quicko.earthquakeapp.websocket.WebSocketPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {


    private final ObjectMapper objectMapper;
    private final WebSocketPublisher webSocketPublisher;

    public KafkaListeners(ObjectMapper objectMapper, WebSocketPublisher webSocketPublisher) {
        this.objectMapper = objectMapper;

        this.webSocketPublisher = webSocketPublisher;
    }

    @KafkaListener(topics = "processed-earthquakes", groupId = "processed-earthquake-consumer")
    public void listener(String data) {
        try {
            Earthquake earthquake = objectMapper.readValue(data, Earthquake.class);
            webSocketPublisher.publishEarthquake(earthquake);
        } catch (Exception e) {
            System.err.println("Error processing log: " + e.getMessage());
        }
    }
}




