package com.viaje.market.service;

import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;

    @Override
    public String getBalance(Integer exchange) {
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
}
