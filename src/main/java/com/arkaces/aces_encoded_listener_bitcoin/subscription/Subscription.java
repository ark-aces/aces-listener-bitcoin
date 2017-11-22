package com.arkaces.aces_encoded_listener_bitcoin.subscription;

import lombok.Data;

@Data
public class Subscription {
    private String identifier;
    private String callbackUrl;
    private Integer minConfirmations;
    private String createdAt;
}
