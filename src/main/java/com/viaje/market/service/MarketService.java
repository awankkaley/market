package com.viaje.market.service;


import com.viaje.market.dto.*;

import java.util.List;

public interface MarketService {
    HotbitBalanceDto getBalance(Integer exchange, String signature);

    HotbitTodayDto getMarketStatusToday(Integer exchange, String signature);

    HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature);

    HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit, String signature);

    GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature);

    GlobalExchangeResponse postMultipleOrder(Integer exchange, OrderMultipleRequestDto orderMultipleRequestDto, String signature);

    GlobalExchangeResponse cancelOrder(Integer exchange, Long orderId, String signature);

    List<OrderResponseDto> getAllGlobalOrder(Integer page, Integer limit, String signature);

    HotbitSuccessResponseDto checkSuccessStatus(Integer exchange, Long orderId, String signature);


}
