package com.viaje.market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "appication.api-key.coinsbit")
@Data
@Configuration
public class CoinsbitConfiguration {
    private String secret;
    private String key;
}
