package com.viaje.market.service;

import com.viaje.market.dto.*;

public interface HotbitService {
    HotbitBalanceResultDto getBalance();

    HotbitTodayResultDto getMarketStatusToday();

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer periode);

    HotbitBookResultDto getListOfTransaction(Integer side, Integer offset, String limit);

    HotbitOrderResultDto postOrder(Integer side, Double amount, Double price, Integer isfee);

}
