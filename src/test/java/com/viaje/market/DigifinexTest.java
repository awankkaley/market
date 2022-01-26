package com.viaje.market;

import com.viaje.market.services.DigifinexServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DigifinexTest {
    @Autowired
    private DigifinexServiceImpl digifinexService;

    @Test
    void getBalance() {
        digifinexService.getBalance();
    }

    @Test
    void getMarketStatus() {
        digifinexService.getMarketStatus();
    }

    @Test
    void postOrder() {
//        buy_market
//        sell_market
        digifinexService.postOrder("sell",1.1234,2.8123);
    }

    @Test
    void cancelOrder() {
        digifinexService.cancelOrder("4d95bc018cf02ab3a1dfc8ff94c1c160");
    }

    @Test
    void getStatus() {
        digifinexService.getStatus("4d95bc018cf02ab3a1dfc8ff94c1c160");
    }


}
