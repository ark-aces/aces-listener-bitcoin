package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_api_server_lib.json.NiceObjectMapper;
import com.arkaces.aces_encoded_listener_bitcoin.event.Event;
import com.arkaces.aces_encoded_listener_bitcoin.subscription.SubscriptionEntity;
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

    public void deliverEvent(SubscriptionEntity subscriptionEntity, Object transaction) {
        // todo: check confirmation depth before posting to subscriber

        String callbackUrl = subscriptionEntity.getCallbackUrl();

        Event event = new Event();
//        event.setRemoteId(transaction.getId());
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
