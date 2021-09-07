package com.viaje.market.service;

import com.viaje.market.dto.HotbitBalanceResultDto;
import com.viaje.market.dto.HotbitBookResultDto;
import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;

public interface HotbitService {
    HotbitBalanceResultDto getBalance();

    HotbitTodayResultDto getMarketStatusToday();

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer periode);

    HotbitBookResultDto getListOfTransaction(Integer side, Integer offset, String limit);


}
