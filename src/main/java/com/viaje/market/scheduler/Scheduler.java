package com.viaje.market.scheduler;

import com.viaje.market.services.MarketService;
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

//    @Scheduled(fixedDelay = 60000)
    public void cronJobCheckStatus() {
        marketService.checkStatusPeriodically();
    }

//    @Scheduled(fixedDelay = 60000*60)
    public void cronJobPendingOrder() {
        marketService.createPendingOrder();
    }
}
