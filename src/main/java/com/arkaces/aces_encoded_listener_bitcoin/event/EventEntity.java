package com.arkaces.aces_encoded_listener_bitcoin.event;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String transactionId;

    @Column(length = 10000)
    private String data;

    private ZonedDateTime createdAt;
}

