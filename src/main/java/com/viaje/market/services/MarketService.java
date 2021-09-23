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
    BalanceResponseDto getBalance(Integer exchange, String signature);

    MarketResponse getMarketStatusToday(Integer exchange, String signature);

    HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature);

    GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature);

    GlobaExchangeMultipleResponse postMultipleOrder(Integer exchange, OrderMultipleRequestDto orderMultipleRequestDto, String signature);

    GlobalExchangeResponse cancelOrder(Integer exchange, Long orderId, String signature);

    List<OrderResponseDto> getAll(Integer page, Integer limit, String signature);

    List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature);

    OrderResponseDto getById(Long orderId, String signature);

    Object getDetailOrder(Long orderId, String signature);



    void checkStatusPeriodically();

    void createPendingOrder();


}
