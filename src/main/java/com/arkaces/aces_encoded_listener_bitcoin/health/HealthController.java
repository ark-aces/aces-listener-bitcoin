package com.arkaces.aces_encoded_listener_bitcoin.health;

import com.arkaces.aces_api_server_lib.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Health getHealth() {
        Health health = new Health();
        health.setStatus("up");
        return health;
    }

}
