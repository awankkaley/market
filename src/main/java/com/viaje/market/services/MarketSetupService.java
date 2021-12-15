package com.viaje.market.services;

import com.viaje.market.dtos.MarketSetupRequestDto;
import com.viaje.market.entities.MarketSetupEntity;
import java.util.List;

public interface MarketSetupService {
    List<MarketSetupEntity> getSetup();

    void updateSetup(MarketSetupRequestDto requestDto, Long id);

}
