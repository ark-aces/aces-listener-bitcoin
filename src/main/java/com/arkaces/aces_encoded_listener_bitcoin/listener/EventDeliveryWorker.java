package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventEntity;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventRepository;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EventDeliveryWorker {

    private final SubscriptionEventRepository subscriptionEventRepository;
    private final EventDeliveryService eventDeliveryService;

    @Scheduled(fixedDelay = 1000L)
    public void run() {
        List<SubscriptionEventEntity> newSubscriptionEventEntities =
                subscriptionEventRepository.findAllByStatus(SubscriptionEventStatus.NEW);
        for (SubscriptionEventEntity subscriptionEventEntity : newSubscriptionEventEntities) {
            eventDeliveryService.trySendEvent(subscriptionEventEntity);
        }
    }

}
