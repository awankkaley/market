package com.viaje.market.service;

import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;

public interface HotbitService {
    String getBalance();

    HotbitTodayResultDto getMarketStatusToday();

    HotbitPeriodResultDto getMarketStatusByPeriode(Integer periode);

}
