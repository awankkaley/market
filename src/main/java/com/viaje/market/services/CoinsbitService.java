package com.viaje.market.services;


import com.viaje.market.dtos.coinsbit_balance.CoinsbitBalanceDto;
import com.viaje.market.dtos.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dtos.coinsbit_status.CoinsbitStatusDto;

public interface CoinsbitService {
    CoinsbitBalanceDto getBalance();

    CoinsbitMarketDto getMarketStatusToday();

    CoinsbitOrderDto postOrder(Integer side, Double amount, Double price);

    CoinsbitOrderDto cancelOrder(Long orderId);

    CoinsbitStatusDto checkSuccessStatus(Long orderId);
}
