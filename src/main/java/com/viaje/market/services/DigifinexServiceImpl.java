package com.viaje.market.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.util.RestUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class DigifinexServiceImpl {

    private RestUtil restUtil;

    public void getBalance() {
        String url = "https://openapi.digifinex.com/v3/spot/assets";
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, "", HttpMethod.GET);
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
        String data = "symbol=btc_usdt";
        String url = "https://openapi.digifinex.com/v3/ticker?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.GET);
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
        String url = "https://openapi.digifinex.com/v3/spot/order/new?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.POST);
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
        String data = "order_id=" + orderId;
        String url = "https://openapi.digifinex.com/v3/spot/order/cancel?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.POST);

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
        String data = "order_id=" + orderId;
        String url = "https://openapi.digifinex.com/v3/spot/order?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.GET);

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
