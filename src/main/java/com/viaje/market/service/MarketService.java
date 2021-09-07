package com.viaje.market.service;


import com.viaje.market.dto.HotbitBalanceResultDto;
import com.viaje.market.dto.HotbitBookResultDto;
import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;

public interface MarketService {
    HotbitBalanceResultDto getBalance(Integer exchange);

    HotbitTodayResultDto getMarketStatusToday(Integer exchange);

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer exchange, Integer periode);

    HotbitBookResultDto getListOfTransaction(Integer exchange, String side, String offset, String limit);


}
