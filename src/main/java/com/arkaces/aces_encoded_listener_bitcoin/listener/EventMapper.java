package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_server.aces_listener.event.Event;
import com.arkaces.aces_server.aces_listener.event.EventEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventMapper {

    private final ObjectMapper objectMapper;

    public Event map(EventEntity eventEntity) {
        JsonNode data;
        try {
            data = objectMapper.readTree(eventEntity.getData());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse event data", e);
        }

        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setTransactionId(eventEntity.getTransactionId());
        event.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).toString());
        event.setData(data);

        return event;
    }
}
