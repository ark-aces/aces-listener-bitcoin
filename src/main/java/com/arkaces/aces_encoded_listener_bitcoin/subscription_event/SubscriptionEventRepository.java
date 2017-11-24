package com.arkaces.aces_encoded_listener_bitcoin.subscription_event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEventEntity, Long> {

    @Query(
        "select se from SubscriptionEventEntity se " +
        "where se.subscriptionEntity.id = :subscriptionEntityId " +
        "and se.eventEntity.transactionId = :transactionId"
    )
    SubscriptionEventEntity findOne(
            @Param("subscriptionEntityId") Long subscriptionEntityId,
            @Param("transactionId") String transactionId
    );

}
