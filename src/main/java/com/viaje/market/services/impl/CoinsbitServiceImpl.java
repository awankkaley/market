package com.viaje.market.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.config.CoinsbitConfiguration;
import com.viaje.market.dtos.CoinsbitSignature;
import com.viaje.market.dtos.coinsbit_balance.CoinsbitBalanceDto;
import com.viaje.market.dtos.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dtos.coinsbit_status.CoinsbitStatusDto;
import com.viaje.market.repositories.CoinbitRepository;
import com.viaje.market.services.CoinsbitService;
import com.viaje.market.util.ConstantValue;
import com.viaje.market.util.SignatureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CoinsbitServiceImpl implements CoinsbitService {
    private CoinsbitConfiguration coinsbitConfiguration;
    private CoinbitRepository coinbitRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CoinsbitBalanceDto getBalance() {
        String request = "/api/v1/account/balances";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
            headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
            headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), CoinsbitBalanceDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException(e.getMessage());
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException(e.getMessage());
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());

        }
    }

    @Override
    public CoinsbitMarketDto getMarketStatusToday() {
        String request = "/api/v1/public/ticker";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            json.put("market", "BSI_USDT");
            CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
            headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
            headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), CoinsbitMarketDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException(e.getMessage());
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException(e.getMessage());
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());

        }
    }


    @Override
    public CoinsbitOrderDto postOrder(String side, Double amount, Double price) {
        String request = "/api/v1/order/new";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            json.put("market", "BSI_USDT");
            json.put("side", side);
            json.put("amount", amount.toString());
            json.put("price", price.toString());
            CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
            headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
            headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            ObjectMapper om = new ObjectMapper();
            String cleanResponse = Objects.requireNonNull(response.getBody()).replace("[[", "").replace("]]", "").replace("[]", "null");
            log.error("--RESPONSE-- : " + cleanResponse);
            CoinsbitOrderDto coinsbitOrderDto = om.readValue(cleanResponse, CoinsbitOrderDto.class);
            if (!coinsbitOrderDto.isSuccess()) {
                return null;
            } else {
                coinbitRepository.save(coinsbitOrderDto.getResult().toEntity());
                return coinsbitOrderDto;
            }
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                return null;
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                return null;
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return null;
        }
    }

    @Override
    public CoinsbitOrderDto cancelOrder(Long orderId) {
        String request = "/api/v1/order/cancel";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            json.put("market", "BSI_USDT");
            json.put("orderId", orderId);
            CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
            headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
            headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(Objects.requireNonNull(response.getBody()).replace("[]", "null"), CoinsbitOrderDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException(e.getMessage());
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException(e.getMessage());
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());

        }
    }

    @Override
    public CoinsbitStatusDto checkSuccessStatus(Long orderId) {
        String request = "/api/v1/account/trades";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            json.put("offset", 0);
            json.put("limit", 5);
            json.put("orderId", orderId);
            CoinsbitSignature coinsbitSignature = SignatureUtil.GenerateSignatureCoinsbit(json.toJSONString(), coinsbitConfiguration);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-TXC-APIKEY", coinsbitConfiguration.getKey());
            headers.set("X-TXC-PAYLOAD", coinsbitSignature.getPayload());
            headers.set("X-TXC-SIGNATURE", coinsbitSignature.getSignature());
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(Objects.requireNonNull(response.getBody()).replace("[]", "null"), CoinsbitStatusDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException(e.getMessage());
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException(e.getMessage());
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException(exp.getMessage());
        }
    }
}
