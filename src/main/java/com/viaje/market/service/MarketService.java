package com.viaje.market.service;


import com.viaje.market.dto.*;

public interface MarketService {
    HotbitBalanceResultDto getBalance(Integer exchange);

    HotbitTodayResultDto getMarketStatusToday(Integer exchange);

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer exchange, Integer periode);

    HotbitBookResultDto getListOfTransaction(Integer exchange, String side, String offset, String limit);

    HotbitOrderResultDto postOrder(Integer exchange, OrderRequestDto orderRequestDto);

    HotbitOrderResultDto cancelOrder(Integer exchange, Long orderId);

}
