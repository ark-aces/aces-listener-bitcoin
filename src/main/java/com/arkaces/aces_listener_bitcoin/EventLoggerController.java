package com.arkaces.aces_listener_bitcoin;

import com.arkaces.aces_server.common.json.NiceObjectMapper;
import com.arkaces.aces_server.aces_listener.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a helper callback endpoint to log raw events for testing purposes.
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventLoggerController {

    private final NiceObjectMapper logObjectMapper;

    @PostMapping("/event-logger")
    public Event postEvent(@RequestBody Event event) {
        log.info(logObjectMapper.writeValueAsString(event));
        return event;
    }
}
