package com.viaje.market.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.dto.HotbitPeriodDto;
import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayDto;
import com.viaje.market.dto.HotbitTodayResultDto;
import com.viaje.market.util.SignatureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    @Override
    public String getBalance() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String sign = SignatureUtil.GenerateSignature("assets=[]");
            log.error(sign);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p2/balance.query")
                    .queryParam("api_key", "7b647764-7280-2c3a-408ac24e3e52ee1c")
                    .queryParam("assets", "[]")
                    .queryParam("sign", sign);
            response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
//                throw new IllegalArgumentException(ConstantValue.invalidAccessToken);
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
//                throw new IllegalArgumentException(ConstantValue.invalidUser);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
//            throw new IllegalArgumentException(ConstantValue.invalidUser);
        }
        return response;
    }

    @Override
    public HotbitTodayResultDto getMarketStatusToday() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HotbitTodayDto hotbitTodayDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status_today")
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitTodayDto = om.readValue(response, HotbitTodayDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
//                throw new IllegalArgumentException(ConstantValue.invalidAccessToken);
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
//                throw new IllegalArgumentException(ConstantValue.invalidUser);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
//            throw new IllegalArgumentException(ConstantValue.invalidUser);
        }

        return hotbitTodayDto.getResult();
    }

    @Override
    public HotbitPeriodResultDto getMarketStatusByPeriode(Integer periode) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HotbitPeriodDto hotbitPeriodDto = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status")
                    .queryParam("period", periode)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            hotbitPeriodDto = om.readValue(response, HotbitPeriodDto.class);
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
                log.error(error.toString());
//                throw new IllegalArgumentException(ConstantValue.invalidAccessToken);
            } catch (IOException mappingExp) {
                log.error(mappingExp.getMessage());
//                throw new IllegalArgumentException(ConstantValue.invalidUser);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
//            throw new IllegalArgumentException(ConstantValue.invalidUser);
        }
        return hotbitPeriodDto.getResult();
    }
}
