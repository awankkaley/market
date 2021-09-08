package com.viaje.market.service;

import com.viaje.market.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;

    @Override
    public HotbitBalanceResultDto getBalance(Integer exchange) {
        if (exchange == 1) {
            return hotbitService.getBalance();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitTodayResultDto getMarketStatusToday(Integer exchange) {
        if (exchange == 1) {
            return hotbitService.getMarketStatusToday();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitPeriodResultDto getMarketStatusByPeriode(Integer exchange, Integer periode) {
        if (exchange == 1) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitBookResultDto getListOfTransaction(Integer exchange, String side, String offset, String limit) {
        if (exchange == 1) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResultDto postOrder(Integer exchange, OrderRequestDto orderRequestDto) {
        if (exchange == 1) {
            return hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice(), orderRequestDto.getIsfee());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitOrderResultDto cancelOrder(Integer exchange, Long orderId) {
        if (exchange == 1) {
            return hotbitService.cancelOrder(orderId);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }
}
