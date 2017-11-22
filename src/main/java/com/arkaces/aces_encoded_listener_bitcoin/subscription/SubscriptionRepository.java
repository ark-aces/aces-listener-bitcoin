package com.arkaces.aces_encoded_listener_bitcoin.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

}
