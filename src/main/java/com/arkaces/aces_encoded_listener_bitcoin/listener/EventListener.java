package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_encoded_listener_bitcoin.bitcoin.BitcoinService;
import com.arkaces.aces_encoded_listener_bitcoin.subscription.SubscriptionEntity;
import com.arkaces.aces_encoded_listener_bitcoin.subscription.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EventListener {

    private final SubscriptionRepository subscriptionRepository;
    private final EventDeliveryService eventDeliveryService;
    private final BitcoinService bitcoinService;

    @Scheduled(fixedDelay = 2000)
    public void scanTransactions() {
        try {
            List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll();

            // Collect transactions
            List<JsonNode> transactions = bitcoinService.getTransactions(2);

            transactions.forEach(x -> log.info("transaction: " + x.toString()));

            // Send transactions to subscribers
            subscriptionEntities.parallelStream().forEach(subscriptionEntity -> {
                transactions.forEach(transaction -> eventDeliveryService.deliverEvent(subscriptionEntity, transaction));
            });
        }
        catch (HttpServerErrorException e) {
            log.error("failed to get transaction data" + e.getResponseBodyAsString());
        }
        catch (Exception e) {
            log.error("Transaction listener threw exception while running", e);
        }
    }

}
