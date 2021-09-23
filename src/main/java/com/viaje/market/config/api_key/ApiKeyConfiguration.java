package com.viaje.market.config.api_key;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "appication.api-key.properties")
@Data
@Configuration
public class ApiKeyConfiguration {
    private String principalRequestHeader;
    private String principalRequestValue;
    private String signRequestHeader;
    private String secretKey;

}
