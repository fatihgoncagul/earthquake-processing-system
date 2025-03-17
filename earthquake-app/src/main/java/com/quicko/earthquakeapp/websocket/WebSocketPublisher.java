package com.quicko.earthquakeapp.websocket;

import com.quicko.earthquakeapp.model.Earthquake;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishEarthquake(Earthquake earthquake) {
        messagingTemplate.convertAndSend("/topic/earthquakes", earthquake);
    }
}

