package com.viaje.market.service;

import com.viaje.market.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;

    @Override
    public HotbitBalanceDto getBalance(Integer exchange) {
        if (exchange == 1) {
            return hotbitService.getBalance();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitTodayDto getMarketStatusToday(Integer exchange) {
        if (exchange == 1) {
            return hotbitService.getMarketStatusToday();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode) {
        if (exchange == 1) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit) {
        if (exchange == 1) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResponseDto postOrder(Integer exchange, OrderRequestDto orderRequestDto) {
        if (exchange == 1) {
            return hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice(), orderRequestDto.getIsfee());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResponseDto cancelOrder(Integer exchange, Long orderId) {
        if (exchange == 1) {
            return hotbitService.cancelOrder(orderId);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }
}
