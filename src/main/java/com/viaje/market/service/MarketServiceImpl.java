package com.viaje.market.service;

import com.viaje.market.api_key.ApiKeyConfiguration;
import com.viaje.market.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;
    private final SignatureService signatureService;
    private final ApiKeyConfiguration apiKeyConfiguration;

    @Override
    public HotbitBalanceDto getBalance(Integer exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getBalance();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitTodayDto getMarketStatusToday(Integer exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getMarketStatusToday();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&period=" + periode;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + side + "&offset=" + offset + "&limit=" + limit;
        log.error(payload);
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResponseDto postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString() + "&price=" + orderRequestDto.getPrice().toString() + "&isfee=" + orderRequestDto.getIsfee();
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice(), orderRequestDto.getIsfee());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResponseDto cancelOrder(Integer exchange, Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.cancelOrder(orderId);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }
}
