package com.arkaces.aces_encoded_listener_bitcoin.listener;

import com.arkaces.aces_encoded_listener_bitcoin.bitcoin.BitcoinService;
import com.arkaces.aces_server.aces_listener.event_delivery.EventDeliveryService;
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

    private final EventDeliveryService eventDeliveryService;
    private final BitcoinService bitcoinService;

    @Scheduled(fixedDelay = 2000)
    public void scanTransactions() {
        try {
            // todo: we should go more than 2 blocks deep to support higher confirmation limits
            List<JsonNode> transactions = bitcoinService.getTransactions(2);
            for (JsonNode transaction : transactions) {
                String transactionId = transaction.get("txid").textValue();
                log.info("Sending transaction with id " + transactionId);
                eventDeliveryService.saveSubscriptionEvents(transactionId, transaction);
            }
        }
        catch (HttpServerErrorException e) {
            log.error("Failed to get transaction data: " + e.getResponseBodyAsString());
        }
        catch (Exception e) {
            log.error("Transaction listener threw exception while running", e);
        }
    }

}
