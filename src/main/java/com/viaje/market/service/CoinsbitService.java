package com.viaje.market.service;


import com.viaje.market.dto.coinsbit_balance.CoinsbitBalanceDto;
import com.viaje.market.dto.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dto.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dto.coinsbit_status.CoinsbitStatusDto;

public interface CoinsbitService {
    CoinsbitBalanceDto getBalance();

    CoinsbitMarketDto getMarketStatusToday();

    CoinsbitOrderDto postOrder(Integer side, Double amount, Double price, Integer isfee);

    CoinsbitOrderDto cancelOrder(Long orderId);

    CoinsbitStatusDto checkSuccessStatus(Long orderId);
}
