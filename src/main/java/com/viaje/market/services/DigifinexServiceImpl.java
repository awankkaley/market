package com.viaje.market.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.dtos.digifinex.*;
import com.viaje.market.repositories.DigifinexRepository;
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
    private DigifinexRepository digifinexRepository;
    private RestUtil restUtil;

    public DigifinexBalanceResponse getBalance() {
        String url = "https://openapi.digifinex.com/v3/spot/assets";
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, "", HttpMethod.GET);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), DigifinexBalanceResponse.class);
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

    public DigifinexMarketResponse getMarketStatus() {
        String data = "symbol=bsi_usdt";
        String url = "https://openapi.digifinex.com/v3/ticker?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.GET);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), DigifinexMarketResponse.class);
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

    public DigifinexStatusResponse postOrder(String type, Double amount, Double price) {
        String data = "symbol=bsi_usdt&type=" + type + "&amount=" + amount + "&price=" + price;
        String url = "https://openapi.digifinex.com/v3/spot/order/new?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.POST);
            ObjectMapper om = new ObjectMapper();
            DigifinexOrderResponse result = om.readValue(response.getBody(), DigifinexOrderResponse.class);
            log.info("DIGIFINEX ORDER :" +result);

            if (result.getCode() != 0) {
                return null;
            } else {
                DigifinexStatusResponse status = getStatus(result.getOrder_id());
                log.info("DIGIFINEX STATUS :" +status.toString());
                digifinexRepository.save(status.getData().get(0).toEntity());
                return status;
            }
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error("HttpClientErrorException :" + error.toString());
                return null;
            } catch (IOException mappingExp) {
                log.error("IOException :" + mappingExp.getMessage());
                return null;
            }
        } catch (Exception exp) {
            log.error("Exception :" + exp.getMessage());
            return null;
        }
    }

    public DigifinexCancelResponse cancelOrder(String orderId) {
        String data = "order_id=" + orderId;
        String url = "https://openapi.digifinex.com/v3/spot/order/cancel?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.POST);
            log.info(response.getBody());
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), DigifinexCancelResponse.class);
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

    public DigifinexStatusResponse getStatus(String orderId) {
        String data = "order_id=" + orderId;
        String url = "https://openapi.digifinex.com/v3/spot/order?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.GET);
            log.info(response.getBody());
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.getBody(), DigifinexStatusResponse.class);
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


    public void currentActiveOrder() {
        String data = "market=spot&symbol=bsi_usdt";
        String url = "https://openapi.digifinex.com/v3/spot/order/current?" + data;
        try {
            ResponseEntity<String> response = restUtil.postDataDigifinex(url, data, HttpMethod.GET);
            log.info(response.getBody());
//            ObjectMapper om = new ObjectMapper();
//            return om.readValue(response.getBody(), DigifinexStatusResponse.class);
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
