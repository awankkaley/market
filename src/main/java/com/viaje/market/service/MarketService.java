package com.viaje.market.service;


import com.viaje.market.dto.*;
import com.viaje.market.dto.coinsbit_order.CoinsbitOrderResultDto;
import com.viaje.market.dto.hotbit_balance.HotbitBalanceDto;
import com.viaje.market.dto.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dto.hotbit_market.HotbitTodayDto;
import com.viaje.market.dto.hotbit_order.HotbitOrderResultDto;
import com.viaje.market.dto.hotbit_order.OrderMultipleRequestDto;
import com.viaje.market.dto.hotbit_order.OrderRequestDto;
import com.viaje.market.dto.hotbit_order.OrderResponseDto;
import com.viaje.market.dto.response.BalanceResponseDto;
import com.viaje.market.dto.response.MarketResponse;

import java.util.List;
import java.util.Optional;

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
