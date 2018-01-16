package com.arkaces.aces_listener_bitcoin.bitcoin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bitcoinRpc")
public class BitcoinRpcSettings {
    private String url;
    private String username;
    private String password;
}
