package com.arkaces.aces_encoded_listener_bitcoin.subscription;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String identifier;
    private String callbackUrl;
    private Integer minConfirmations;
    private String status;
    private ZonedDateTime createdAt;

}
