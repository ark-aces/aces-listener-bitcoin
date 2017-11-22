package com.arkaces.aces_encoded_listener_bitcoin.subscription;

import lombok.Data;

@Data
public class CreateSubscriptionRequest {
    private String callbackUrl;
    private Integer minConfirmations;
}
