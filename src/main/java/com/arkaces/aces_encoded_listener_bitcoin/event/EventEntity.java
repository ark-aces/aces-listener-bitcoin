package com.arkaces.aces_encoded_listener_bitcoin.event;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String remoteId;

    private String data;

    private ZonedDateTime createdAt;
}

