package com.viaje.market.util;

import com.viaje.market.config.CoinsbitConfiguration;
import com.viaje.market.config.DigifinexConfiguration;
import com.viaje.market.dtos.CoinsbitSignature;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class RestUtil {
    private final RestTemplate restTemplate;
    private final CoinsbitConfiguration coinsbitConfiguration;
    private final DigifinexConfiguration digifinexConfiguration;


    public RestUtil(RestTemplateBuilder restTemplateBuilder, CoinsbitConfiguration coinsbitConfiguration, DigifinexConfiguration digifinexConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.coinsbitConfiguration = coinsbitConfiguration;
        this.digifinexConfiguration = digifinexConfiguration;
    }

    public ResponseEntity<String> postDataCoinsbit(String url, JSONObject json) {
        CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
        headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
        headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }


    public ResponseEntity<String> postDataDigifinex(String url, String data, HttpMethod httpMethod) {
        String sign = SignatureUtil.GenerateSignatureDigifinex(data, digifinexConfiguration);
        HttpHeaders headers = new HttpHeaders();
        headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
        headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
        headers.set("ACCESS-SIGN", sign);

        HttpEntity request = new HttpEntity(headers);

        return  restTemplate.exchange(
                url,
                httpMethod,
                request,
                String.class,
                1
        );
    }
}
