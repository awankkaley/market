package com.viaje.market.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.dtos.coinsbit_balance.CoinsbitBalanceDto;
import com.viaje.market.dtos.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dtos.coinsbit_status.CoinsbitStatusDto;
import com.viaje.market.repositories.CoinbitRepository;
import com.viaje.market.util.RestUtil;
import com.viaje.market.util.ConstantValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CoinsbitServiceImpl {
    private CoinbitRepository coinbitRepository;
    private RestUtil restUtil ;


    public CoinsbitBalanceDto getBalance() {
        String request = "/api/v1/account/balances";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());

            ResponseEntity<String> response = restUtil.postDataCoinsbit(fullUrl, json);
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

    public CoinsbitMarketDto getMarketStatusToday() {
        String request = "/api/v1/public/ticker";
        String fullUrl = ConstantValue.BASE_URL + request;
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            JSONObject json = new JSONObject();
            json.put("request", request);
            json.put("nonce", timestamp.getTime());
            json.put("market", "BSI_USDT");
            ResponseEntity<String> response = restUtil.postDataCoinsbit(fullUrl, json);
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
            ResponseEntity<String> response = restUtil.postDataCoinsbit(fullUrl, json);
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
            ResponseEntity<String> response = restUtil.postDataCoinsbit(fullUrl, json);
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
            ResponseEntity<String> response = restUtil.postDataCoinsbit(fullUrl, json);
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
