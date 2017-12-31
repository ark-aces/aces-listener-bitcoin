package com.arkaces.aces_listener_bitcoin;

import com.arkaces.aces_listener_bitcoin.bitcoin.BitcoinService;
import com.arkaces.aces_server.aces_listener.event_delivery.EventDeliveryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
public class BitcoinEventListener {

    private final EventDeliveryService eventDeliveryService;
    private final BitcoinService bitcoinService;

    @Scheduled(fixedDelay = 2000)
    public void scanTransactions() {
        try {
            List<JsonNode> transactions = bitcoinService.getTransactions(4);
            for (JsonNode transaction : transactions) {
                String transactionId = transaction.get("txid").textValue();

                Integer confirmations = transaction.get("confirmations").asInt();

                if (transaction.has("vout")) {
                    ArrayNode vouts = (ArrayNode) transaction.get("vout");
                    for (JsonNode vout : vouts) {
                        if (vout.has("scriptPubKey")) {
                            JsonNode scriptPubKey = vout.get("scriptPubKey");
                            if (scriptPubKey.has("addresses")) {
                                ArrayNode recipientAddressNodes = (ArrayNode) scriptPubKey.get("addresses");
                                for (JsonNode recipientAddressNode : recipientAddressNodes) {
                                    String recipientAddress = recipientAddressNode.textValue();
                                    eventDeliveryService.saveSubscriptionEvents(
                                            transactionId,
                                            recipientAddress,
                                            confirmations,
                                            transaction
                                    );
                                }
                            }
                        }
                    }
                }
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
