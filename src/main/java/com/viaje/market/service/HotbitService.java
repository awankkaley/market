package com.viaje.market.service;

import com.viaje.market.dto.*;

public interface HotbitService {
    HotbitBalanceDto getBalance();

    HotbitTodayDto getMarketStatusToday();

    HotbitPeriodDto getMarketStatusByPeriode(Integer periode);

    HotbitBookDto getListOfTransaction(Integer side, Integer offset, String limit);

    HotbitOrderResponseDto postOrder(Integer side, Double amount, Double price, Integer isfee);

    HotbitOrderResponseDto cancelOrder(Long orderId);


}
