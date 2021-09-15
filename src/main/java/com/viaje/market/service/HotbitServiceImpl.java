package com.viaje.market.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.config.HotbitConfiguration;
import com.viaje.market.dto.*;
import com.viaje.market.util.SignatureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class HotbitServiceImpl implements HotbitService {

    private HotbitConfiguration hotbitConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public HotbitBalanceDto getBalance() {
        HotbitBalanceDto hotbitBalanceDto = null;
        try {
            String data = "api_key=" + hotbitConfiguration.getKey() + "&assets=[]";
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            String url = "https://api.hotbit.io/v2/p2/balance.query?api_key=" + hotbitConfiguration.getKey() + "&" + data + "&sign=" + sign;
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitBalanceDto = om.readValue(response, HotbitBalanceDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");

        }
        return hotbitBalanceDto;
    }

    @Override
    public HotbitTodayDto getMarketStatusToday() {
        HotbitTodayDto hotbitTodayDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status_today")
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
            ObjectMapper om = new ObjectMapper();
            hotbitTodayDto = om.readValue(response, HotbitTodayDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");
        }
        return hotbitTodayDto;
    }

    @Override
    public HotbitPeriodDto getMarketStatusByPeriode(Integer periode) {
        HotbitPeriodDto hotbitPeriodDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status")
                    .queryParam("period", periode)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
            ObjectMapper om = new ObjectMapper();
            hotbitPeriodDto = om.readValue(response, HotbitPeriodDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");
        }
        return hotbitPeriodDto;
    }

    @Override
    public HotbitBookDto getListOfTransaction(Integer side, Integer offset, String limit) {
        HotbitBookDto hotbitPeriodDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/order.book")
                    .queryParam("side", side)
                    .queryParam("offset", offset)
                    .queryParam("limit", limit)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
            ObjectMapper om = new ObjectMapper();
            hotbitPeriodDto = om.readValue(response, HotbitBookDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");
        }
        return hotbitPeriodDto;
    }

    @Override
    public HotbitOrderResponseDto postOrder(Integer side, Double amount, Double price, Integer isfee) {
        HotbitOrderResponseDto hotbitOrderResponseDto = null;
        try {
            String data = "amount=" + amount + "&api_key=" + hotbitConfiguration.getKey() + "&isfee=" + isfee + "&market=BSI/USDT&price=" + price + "&side=" + side;
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p2/order.put_limit")
                    .queryParam("api_key", hotbitConfiguration.getKey())
                    .queryParam("side", side)
                    .queryParam("amount", amount)
                    .queryParam("price", price)
                    .queryParam("isfee", isfee)
                    .queryParam("sign", sign)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitOrderResponseDto = om.readValue(response, HotbitOrderResponseDto.class);
            if (hotbitOrderResponseDto.getError() != null){
                return null;
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
        return hotbitOrderResponseDto;
    }

    @Override
    public HotbitOrderResponseDto cancelOrder(Long orderId) {
        HotbitOrderResponseDto hotbitOrderResponseDto = null;
        try {
            String data = "api_key=" + hotbitConfiguration.getKey() + "&market=BSI/USDT&order_id=" + orderId;
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p2/order.cancel")
                    .queryParam("api_key", hotbitConfiguration.getKey())
                    .queryParam("order_id", orderId)
                    .queryParam("sign", sign)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitOrderResponseDto = om.readValue(response, HotbitOrderResponseDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");
        }
        return hotbitOrderResponseDto;
    }

    @Override
    public HotbitSuccessResponseDto checkSuccessStatus(Long orderId) {
        HotbitSuccessResponseDto hotbitSuccessResponseDto = null;
        try {
            String data = "api_key=" + hotbitConfiguration.getKey() + "&offset=0&order_id=" + orderId;
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p2/order.finished_detail")
                    .queryParam("api_key", hotbitConfiguration.getKey())
                    .queryParam("order_id", orderId)
                    .queryParam("sign", sign)
                    .queryParam("offset", "0");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitSuccessResponseDto = om.readValue(response, HotbitSuccessResponseDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
                throw new IllegalArgumentException("Failed to access Hotbit");
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
                throw new IllegalArgumentException("Failed to access Hotbit");
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            throw new IllegalArgumentException("Failed to access Hotbit");
        }
        return hotbitSuccessResponseDto;
    }


}
