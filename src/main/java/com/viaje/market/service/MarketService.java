package com.viaje.market.service;


import com.viaje.market.dto.*;

public interface MarketService {
    HotbitBalanceDto getBalance(Integer exchange);

    HotbitTodayDto getMarketStatusToday(Integer exchange);

    HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode);

    HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit);

    HotbitOrderResponseDto postOrder(Integer exchange, OrderRequestDto orderRequestDto);

    HotbitOrderResponseDto cancelOrder(Integer exchange, Long orderId);

}
