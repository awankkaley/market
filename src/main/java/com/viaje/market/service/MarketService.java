package com.viaje.market.service;


import com.viaje.market.dto.*;

public interface MarketService {
    HotbitBalanceDto getBalance(Integer exchange, String signature);

    HotbitTodayDto getMarketStatusToday(Integer exchange, String signature);

    HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature);

    HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit, String signature);

    HotbitOrderResponseDto postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature);

    HotbitOrderResponseDto cancelOrder(Integer exchange, Long orderId, String signature);

}
