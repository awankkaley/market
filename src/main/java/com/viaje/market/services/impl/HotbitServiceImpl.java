package com.viaje.market.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.config.HotbitConfiguration;
import com.viaje.market.dtos.hotbit_balance.HotbitBalanceDto;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dtos.hotbit_market.HotbitTodayDto;
import com.viaje.market.dtos.hotbit_order.HotbitOrderResponseDto;
import com.viaje.market.dtos.hotbit_status.HotbitSuccessResponseDto;
import com.viaje.market.repositories.HotbitRepository;
import com.viaje.market.services.HotbitService;
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
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class HotbitServiceImpl implements HotbitService {

    private HotbitConfiguration hotbitConfiguration;
    private HotbitRepository hotbitRepository;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public HotbitBalanceDto getBalance() {
        try {
            String data = "api_key=" + hotbitConfiguration.getKey() + "&assets=[]";
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            String url = "https://api.hotbit.io/v2/p2/balance.query?api_key=" + hotbitConfiguration.getKey() + "&" + data + "&sign=" + sign;
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response, HotbitBalanceDto.class);
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
    public HotbitTodayDto getMarketStatusToday() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status_today")
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response, HotbitTodayDto.class);
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
    public HotbitPeriodDto getMarketStatusByPeriode(Integer periode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/market.status")
                    .queryParam("period", periode)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(response, HotbitPeriodDto.class);
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
    public HotbitOrderResponseDto postOrder(String side, Double amount, Double price) {
        int sideInt = 1;
        if (Objects.equals(side, "buy")) {
            sideInt = 2;
        }
        try {
            String data = "amount=" + amount + "&api_key=" + hotbitConfiguration.getKey() + "&isfee=" + 0 + "&market=BSI/USDT&price=" + price + "&side=" + sideInt;
            String sign = SignatureUtil.GenerateSignatureHotbit(data, hotbitConfiguration);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p2/order.put_limit")
                    .queryParam("api_key", hotbitConfiguration.getKey())
                    .queryParam("side", sideInt)
                    .queryParam("amount", amount)
                    .queryParam("price", price)
                    .queryParam("isfee", 0)
                    .queryParam("sign", sign)
                    .queryParam("market", "BSI/USDT");
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            ObjectMapper om = new ObjectMapper();
            HotbitOrderResponseDto hotbitOrderResponseDto = om.readValue(response, HotbitOrderResponseDto.class);
            if (hotbitOrderResponseDto.getError() != null) {
                return null;
            } else {
                hotbitRepository.save(hotbitOrderResponseDto.getResult().toEntity());
                return hotbitOrderResponseDto;
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
    public HotbitOrderResponseDto cancelOrder(Long orderId) {
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
            return om.readValue(response, HotbitOrderResponseDto.class);
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
    public HotbitSuccessResponseDto checkSuccessStatus(Long orderId) {
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
            return om.readValue(response, HotbitSuccessResponseDto.class);
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


//    @Override
//    public HotbitBookDto getListOfTransaction(Integer side, Integer offset, String limit) {
//        HotbitBookDto hotbitPeriodDto = null;
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.hotbit.io/v2/p1/order.book")
//                    .queryParam("side", side)
//                    .queryParam("offset", offset)
//                    .queryParam("limit", limit)
//                    .queryParam("market", "BSI/USDT");
//            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class, headers);
//            ObjectMapper om = new ObjectMapper();
//            hotbitPeriodDto = om.readValue(response, HotbitBookDto.class);
//        } catch (HttpClientErrorException e) {
//            try {
//                JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
//                log.error(error.toString());
//                throw new IllegalArgumentException("Failed to access Hotbit");
//            } catch (IOException mappingExp) {
//                log.error(mappingExp.getMessage());
//                throw new IllegalArgumentException("Failed to access Hotbit");
//            }
//        } catch (Exception exp) {
//            log.error(exp.getMessage());
//            throw new IllegalArgumentException("Failed to access Hotbit");
//        }
//        return hotbitPeriodDto;
//    }

}
