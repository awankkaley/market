package com.viaje.market.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.config.DigifinexConfiguration;
import com.viaje.market.dtos.hotbit_balance.HotbitBalanceDto;
import com.viaje.market.util.SignatureUtil;
import com.viaje.market.util.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

@Slf4j
@Service
@AllArgsConstructor
public class DigifinexServiceImpl {

    private DigifinexConfiguration digifinexConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    public void getBalance() throws UnsupportedEncodingException {
        String data = URLEncoder.encode("", "utf-8");
        String sign = SignatureUtil.GenerateSignatureDigifinex(data, digifinexConfiguration);
        String url = "https://openapi.digifinex.com/v3/spot/assets";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
            headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
            headers.set("ACCESS-SIGN", sign);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            log.error(response.getBody());
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                throw new IllegalArgumentException(error.toString());
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                throw new IllegalArgumentException(mappingExp.getMessage());
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }

    public void getMarketStatus() {
        String sign = SignatureUtil.GenerateSignatureDigifinex("symbol=btc_usdt", digifinexConfiguration);
        String url = "https://openapi.digifinex.com/v3/ticker?symbol=btc_usdt";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
            headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
            headers.set("ACCESS-SIGN", sign);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            log.error(response.getBody());
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                throw new IllegalArgumentException(error.toString());
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                throw new IllegalArgumentException(mappingExp.getMessage());
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }

    public void postOrder(String type, Double amount, Double price) {
        String data = "symbol=btc_usdt&type=" + type + "&amount=" + amount + "&price=" + price;
        String sign = SignatureUtil.GenerateSignatureDigifinex(data, digifinexConfiguration);
        String url = "https://openapi.digifinex.com/v3/spot/order/new?"+data;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
            headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
            headers.set("ACCESS-SIGN", sign);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class,
                    1
            );
            log.error(response.getBody());
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                throw new IllegalArgumentException(error.toString());
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                throw new IllegalArgumentException(mappingExp.getMessage());
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }

    public void cancelOrder(String orderId) {
        String data = "order_id="+orderId;
        String sign = SignatureUtil.GenerateSignatureDigifinex(data, digifinexConfiguration);
        String url = "https://openapi.digifinex.com/v3/spot/order/cancel?"+data;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
            headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
            headers.set("ACCESS-SIGN", sign);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class,
                    1
            );
            log.error(response.getBody());
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                throw new IllegalArgumentException(error.toString());
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                throw new IllegalArgumentException(mappingExp.getMessage());
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }

    public void getStatus(String orderId) {
        String data = "order_id="+orderId;
        String sign = SignatureUtil.GenerateSignatureDigifinex(data, digifinexConfiguration);
        String url = "https://openapi.digifinex.com/v3/spot/order?"+data;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("ACCESS-KEY", digifinexConfiguration.getKey());
            headers.set("ACCESS-TIMESTAMP", Util.getCurrentTimeSecond());
            headers.set("ACCESS-SIGN", sign);

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            log.error(response.getBody());
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                throw new IllegalArgumentException(error.toString());
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                throw new IllegalArgumentException(mappingExp.getMessage());
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }
}
