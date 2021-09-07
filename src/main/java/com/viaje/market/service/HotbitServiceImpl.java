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
    public HotbitBalanceResultDto getBalance() {
        HotbitBalanceDto hotbitBalanceDto = null;
        try {
            String data = "assets=[]";
            String sign = SignatureUtil.GenerateSignature(data, hotbitConfiguration);
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
        return hotbitBalanceDto.getResult();
    }

    @Override
    public HotbitTodayResultDto getMarketStatusToday() {
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
        return hotbitTodayDto.getResult();
    }

    @Override
    public HotbitPeriodResultDto getMarketStatusByPeriode(Integer periode) {
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
        return hotbitPeriodDto.getResult();
    }

    @Override
    public HotbitBookResultDto getListOfTransaction(Integer side, Integer offset, String limit) {
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
        return hotbitPeriodDto.getResult();
    }
}
