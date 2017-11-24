package com.arkaces.aces_encoded_listener_bitcoin.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends JpaRepository<EventEntity, Long> {

}
