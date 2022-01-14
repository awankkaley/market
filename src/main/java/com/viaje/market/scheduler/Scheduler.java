package com.viaje.market.scheduler;

import com.viaje.market.services.MarketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final MarketServiceImpl marketService;

    @Autowired
    public Scheduler(MarketServiceImpl marketService) {
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
