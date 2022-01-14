package com.viaje.market.services;

import com.viaje.market.dtos.MarketSetupRequestDto;
import com.viaje.market.entities.MarketSetupEntity;
import com.viaje.market.repositories.MarketSetupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MarketSetupServiceImpl {
    private MarketSetupRepository marketSetupRepository;

    public List<MarketSetupEntity> getSetup() {
        return marketSetupRepository.findAll();
    }

    public void updateSetup(MarketSetupRequestDto requestDto, Long id) {
        MarketSetupEntity marketSetup = marketSetupRepository.getById(id);
        marketSetup.setBuyPercent(requestDto.getBuyPercent());
        marketSetup.setProfitPercent(requestDto.getProfitPercent());
        marketSetupRepository.save(marketSetup);
    }
}
