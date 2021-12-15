package com.viaje.market.services.impl;

import com.viaje.market.dtos.MarketSetupRequestDto;
import com.viaje.market.entities.MarketSetupEntity;
import com.viaje.market.repositories.MarketSetupRepository;
import com.viaje.market.services.MarketSetupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MarketSetupServiceImpl implements MarketSetupService {
    private MarketSetupRepository marketSetupRepository;

    @Override
    public List<MarketSetupEntity> getSetup() {
        return marketSetupRepository.findAll();
    }

    @Override
    public void updateSetup(MarketSetupRequestDto requestDto, Long id) {
        MarketSetupEntity marketSetup = marketSetupRepository.getById(id);
        marketSetup.setBuyPercent(requestDto.getBuyPercent());
        marketSetup.setProfitPercent(requestDto.getProfitPercent());
        marketSetupRepository.save(marketSetup);
    }
}
