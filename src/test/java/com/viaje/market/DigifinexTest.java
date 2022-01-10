package com.viaje.market;

import com.viaje.market.services.impl.DigifinexServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class DigifinexTest {
    @Autowired
    private DigifinexServiceImpl digifinexService;

    @Test
    void getBalance() throws UnsupportedEncodingException {
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
        digifinexService.postOrder("buy_market",1.00,1.00);
    }

    @Test
    void cancelOrder() {
        digifinexService.cancelOrder("198361cecdc65f9c8c9bb2fa68faec40");
    }

    @Test
    void getStatus() {
        digifinexService.getStatus("198361cecdc65f9c8c9bb2fa68faec40");
    }


}
