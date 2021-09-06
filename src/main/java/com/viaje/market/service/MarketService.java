package com.viaje.market.service;


import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;

public interface MarketService {
    String getBalance(Integer exchange);

    HotbitTodayResultDto getMarketStatusToday(Integer exchange);

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer exchange, Integer periode);
}
