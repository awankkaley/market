package com.viaje.market;

import com.viaje.market.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final MarketService marketService;

    @Autowired
    public Scheduler(MarketService marketService) {
        this.marketService = marketService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void cronJobCheckStatus(){
        log.info("-------schedule running-------");
        marketService.checkStatusPeriodically();
    }
}
