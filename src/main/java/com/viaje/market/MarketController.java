package com.viaje.market;

import com.viaje.market.base_dto.GlobalDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/market")
public class MarketController {

    @PostMapping("/buy/{exchangeCode}")
    public GlobalDto<String> buy(@Valid @PathVariable Integer exchangeCode) {
        return new GlobalDto<>(
                9000,
                HttpStatus.CREATED.getReasonPhrase(),
                "BUY" + exchangeCode
        );
    }

    @PostMapping("/sell/{exchangeCode}")
    public GlobalDto<String> sell(@Valid @PathVariable Integer exchangeCode) {
        return new GlobalDto<>(
                9000,
                HttpStatus.CREATED.getReasonPhrase(),
                "SELL" + exchangeCode
        );
    }
}
