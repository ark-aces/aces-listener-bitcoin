package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_server.aces_listener.subscription.SubscriptionRepository;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionStatus;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventStatus;
import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import com.arkaces.aces_server.aces_listener.event.Event;
import com.arkaces.aces_server.aces_listener.event.EventEntity;
import com.arkaces.aces_server.aces_listener.event.EventRepository;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventEntity;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
public class EventDeliveryService {

    private final IdentifierGenerator identifierGenerator;
    private final RestTemplate callbackRestTemplate;
    private final EventRepository eventRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionEventRepository subscriptionEventRepository;
    private final EventMapper eventMapper;

    public void saveSubscriptionEvent(SubscriptionEntity subscriptionEntity, String transactionId, JsonNode data) {
        Long subscriptionEntityPid = subscriptionEntity.getPid();

        SubscriptionEventEntity existingSubscriptionEventEntity
                = subscriptionEventRepository.findOne(subscriptionEntityPid, transactionId);
        if (existingSubscriptionEventEntity == null) {
            log.info("Creating new subscription event for subscription " + subscriptionEntity.getId() +
                " and transaction id " + transactionId);

            EventEntity eventEntity = new EventEntity();
            eventEntity.setId(identifierGenerator.generate());
            eventEntity.setCreatedAt(ZonedDateTime.now());
            eventEntity.setTransactionId(transactionId);
            eventEntity.setData(data.toString());
            eventRepository.save(eventEntity);

            SubscriptionEventEntity subscriptionEventEntity = new SubscriptionEventEntity();
            subscriptionEventEntity.setCreatedAt(ZonedDateTime.now());
            subscriptionEventEntity.setSubscriptionEntity(subscriptionEntity);
            subscriptionEventEntity.setEventEntity(eventEntity);
            subscriptionEventEntity.setStatus(SubscriptionEventStatus.NEW);
            subscriptionEventEntity.setTries(0);
            subscriptionEventRepository.save(subscriptionEventEntity);

            log.info("Saved subscription event entity " + subscriptionEventEntity.getPid());
        }
    }

    public void trySendEvent(SubscriptionEventEntity subscriptionEventEntity) {
        SubscriptionEntity subscriptionEntity = subscriptionEventEntity.getSubscriptionEntity();

        EventEntity eventEntity = subscriptionEventEntity.getEventEntity();

        if (subscriptionEventEntity.getTries() > 10) {
            log.info("Subscription event " + subscriptionEntity.getId() + " tried too many times - setting to FAILED");
            subscriptionEventEntity.setStatus(SubscriptionEventStatus.FAILED);

            // todo: might want to cancel subscriptions less aggressively
            // Here we cancel a subscription if it fails more than 10 times 1 second apart
            subscriptionEntity.setStatus(SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(subscriptionEntity);
        } else {
            subscriptionEventEntity.setTries(subscriptionEventEntity.getTries() + 1);

            log.info("Trying to send subscription event " + subscriptionEventEntity.getPid() + " (try "
                    + subscriptionEventEntity.getTries() + ")");

            Event event = eventMapper.map(eventEntity);
            String callbackUrl = subscriptionEntity.getCallbackUrl();
            try {
                ResponseEntity<String> response = callbackRestTemplate.postForEntity(callbackUrl, event, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Delivered event " + event.getId() + " to subscriber " + subscriptionEntity.getId());
                    subscriptionEventEntity.setStatus(SubscriptionEventStatus.DELIVERED);
                    subscriptionEventRepository.save(subscriptionEventEntity);
                } else {
                    log.info("Subscription event post returned non-200 response code and will retry later");
                }
            } catch (RestClientResponseException e) {
                log.warn("Failed to post event to subscriber: " + e.getResponseBodyAsString());
            } catch (Exception e) {
                log.warn("Failed to post event to subscriber", e);
            }
        }

        subscriptionEventRepository.save(subscriptionEventEntity);
    }

}
