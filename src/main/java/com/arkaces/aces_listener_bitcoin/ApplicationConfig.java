package com.arkaces.aces_listener_bitcoin;

import com.arkaces.aces_listener_bitcoin.bitcoin.BitcoinRpcSettings;
import com.arkaces.aces_server.aces_listener.config.AcesListenerConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@Import(AcesListenerConfig.class)
public class ApplicationConfig {

    @Bean
    public Integer maxScanBlockDepth(Environment environment) {
        return environment.getProperty("maxScanBlockDepth", Integer.class);
    }

    @Bean
    public RestTemplate bitcoinRpcRestTemplate(BitcoinRpcSettings bitcoinRpcSettings) {
        return new RestTemplateBuilder()
            .rootUri("http://localhost:18332/")
            .basicAuthorization(bitcoinRpcSettings.getUsername(), bitcoinRpcSettings.getPassword())
            .build();
    }

}
