package com.arkaces.aces_encoded_listener_bitcoin.event;

import lombok.Data;

@Data
public class Event {
    private String remoteId;
    private String data;
    private String createdAt;
}
