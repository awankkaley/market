package com.viaje.market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "appication.api-key.hotbit")
@Data
@Configuration
public class HotbitConfiguration {
    private String secret;
    private String key;
}
