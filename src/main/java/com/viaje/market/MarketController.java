package com.viaje.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viaje.market.base_dto.GlobalDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/market")
public class MarketController {

    @PostMapping("/buy/{exchangeCode}")
    public GlobalDto<String> buy(@Valid @PathVariable Integer exchangeCode, @RequestBody UjiData ujiData) {
        return new GlobalDto<>(
                9000,
                HttpStatus.CREATED.getReasonPhrase(),
                "BUY" + exchangeCode
        );
    }

    @PostMapping("/sell/{exchangeCode}")
    public GlobalDto<String> sell(@Valid @PathVariable Integer exchangeCode, @RequestBody UjiData ujiData) {
        log.error("--REQQUEST--" + ujiData);
        return new GlobalDto<>(
                9000,
                HttpStatus.CREATED.getReasonPhrase(),
                "SELL" + exchangeCode
        );
    }
}
