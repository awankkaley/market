package com.viaje.market.service;


import com.viaje.market.dto.*;

import java.util.List;

public interface MarketService {
    HotbitBalanceDto getBalance(Integer exchange, String signature);

    HotbitTodayDto getMarketStatusToday(Integer exchange, String signature);

    HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature);

    GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature);

    GlobaExchangeMultipleResponse postMultipleOrder(Integer exchange, OrderMultipleRequestDto orderMultipleRequestDto, String signature);

    GlobalExchangeResponse cancelOrder(Integer exchange, Long orderId, String signature);

    List<OrderResponseDto> getAll(Integer page, Integer limit, String signature);

    List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature);

    OrderResponseDto getById(Long orderId, String signature);

    void checkStatusPeriodically();

}
