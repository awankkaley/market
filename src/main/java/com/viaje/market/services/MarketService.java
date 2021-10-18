package com.viaje.market.services;


import com.viaje.market.dtos.*;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dtos.hotbit_order.OrderMultipleRequestDto;
import com.viaje.market.dtos.hotbit_order.OrderRequestDto;
import com.viaje.market.dtos.hotbit_order.OrderResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import com.viaje.market.dtos.response.MarketResponse;

import java.util.List;

public interface MarketService {
    BalanceResponseDto getBalance(String exchange, String signature);

    MarketResponse getMarketStatusToday(String exchange, String signature);

    HotbitPeriodDto getMarketStatusByPeriode(String exchange, Integer periode, String signature);

    GlobalExchangeResponse postOrder(String exchange, OrderRequestDto orderRequestDto, String signature);

    GlobaExchangeMultipleResponse postMultipleOrder(String exchange, OrderMultipleRequestDto orderMultipleRequestDto, String signature);

    GlobalExchangeResponse cancelOrder(String exchange, Long orderId, String signature);

    List<OrderResponseDto> getAll(Integer page, Integer limit, String signature);

    List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature);

    OrderResponseDto getById(Long orderId, String signature);

    Object getDetailOrder(Long orderId, String signature);



    void checkStatusPeriodically();

    void createPendingOrder();


}
