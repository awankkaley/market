package com.viaje.market.service;

import com.viaje.market.dto.hotbit_balance.HotbitBalanceDto;
import com.viaje.market.dto.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dto.hotbit_market.HotbitTodayDto;
import com.viaje.market.dto.hotbit_order.HotbitBookDto;
import com.viaje.market.dto.hotbit_order.HotbitOrderResponseDto;
import com.viaje.market.dto.hotbit_status.HotbitSuccessResponseDto;

public interface HotbitService {
    HotbitBalanceDto getBalance();

    HotbitTodayDto getMarketStatusToday();

    HotbitPeriodDto getMarketStatusByPeriode(Integer periode);

    HotbitOrderResponseDto postOrder(Integer side, Double amount, Double price);

    HotbitOrderResponseDto cancelOrder(Long orderId);

    HotbitSuccessResponseDto checkSuccessStatus(Long orderId);



}
