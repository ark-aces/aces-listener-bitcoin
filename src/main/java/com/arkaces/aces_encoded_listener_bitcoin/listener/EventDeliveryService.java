package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_api_server_lib.json.NiceObjectMapper;
import com.arkaces.aces_encoded_listener_bitcoin.event.Event;
import com.arkaces.aces_encoded_listener_bitcoin.event.EventEntity;
import com.arkaces.aces_encoded_listener_bitcoin.event.EventRepository;
import com.arkaces.aces_encoded_listener_bitcoin.subscription.SubscriptionEntity;
import com.arkaces.aces_encoded_listener_bitcoin.subscription_event.SubscriptionEventEntity;
import com.arkaces.aces_encoded_listener_bitcoin.subscription_event.SubscriptionEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EventDeliveryService {

    private final RestTemplate callbackRestTemplate;
    private final NiceObjectMapper dtoObjectMapper;
    private final EventRepository eventRepository;
    private final SubscriptionEventRepository subscriptionEventRepository;

    public void deliverEvent(SubscriptionEntity subscriptionEntity, JsonNode transaction) {
        // todo: check confirmation depth before posting to subscriber

        Long subscriptionEntityId = subscriptionEntity.getId();
        String transactionId = transaction.get("txid").textValue();

        SubscriptionEventEntity existingSubscriptionEventEntity
                = subscriptionEventRepository.findOne(subscriptionEntityId, transactionId);
        if (existingSubscriptionEventEntity == null) {
            EventEntity eventEntity = new EventEntity();
            eventEntity.setCreatedAt(ZonedDateTime.now());
            eventEntity.setTransactionId(transactionId);
            eventEntity.setData(transaction.toString());
            eventRepository.save(eventEntity);

            SubscriptionEventEntity subscriptionEventEntity = new SubscriptionEventEntity();
            subscriptionEventEntity.setCreatedAt(ZonedDateTime.now());
            subscriptionEventEntity.setSubscriptionEntity(subscriptionEntity);
            subscriptionEventEntity.setEventEntity(eventEntity);
            subscriptionEventEntity.setStatus(SubscriptionEventStatus.NEW);
            subscriptionEventEntity.setTries(0);
            subscriptionEventRepository.save(subscriptionEventEntity);


            String callbackUrl = subscriptionEntity.getCallbackUrl();

            Event event = new Event();
            event.setTransactionId(transactionId);
            event.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).toString());
            event.setData(dtoObjectMapper.writeValueAsString(transaction));

            ResponseEntity<String> response = callbackRestTemplate.postForEntity(callbackUrl, event, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // todo: mark the subscription event as delivered so we don't resend
            } else {
                // todo: the callback URL did not accept the POST successfully, we should retry here or
                // queue up the event for later retries.
                log.info("Failed callback response: " + response.toString());
                log.warn("Post to callback URL for subscription ID '{}' failed");
            }
        }


    }
}
