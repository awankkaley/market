package com.viaje.market;

import com.viaje.market.base_dto.GlobalDto;
import com.viaje.market.dto.HotbitPeriodResultDto;
import com.viaje.market.dto.HotbitTodayResultDto;
import com.viaje.market.service.MarketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class MarketController {
    private final MarketService marketService;

    @PostMapping("/balance/{exchangeCode}")
    public GlobalDto<String> balance(@Valid @PathVariable Integer exchangeCode) {
        return new GlobalDto<>(
                9000,
                HttpStatus.OK.getReasonPhrase(),
                marketService.getBalance(exchangeCode)
        );
    }

    @PostMapping("/market/today/{exchangeCode}")
    public GlobalDto<HotbitTodayResultDto> marketToday(@Valid @PathVariable Integer exchangeCode) {
        return new GlobalDto<>(
                9000,
                HttpStatus.OK.getReasonPhrase(),
                marketService.getMarketStatusToday(exchangeCode)
        );
    }

    @PostMapping("/market/period/{exchangeCode}/{period}")
    public GlobalDto<HotbitPeriodResultDto> marketPeriod(@Valid @PathVariable Integer exchangeCode, @PathVariable Integer period) {
        return new GlobalDto<>(
                9000,
                HttpStatus.OK.getReasonPhrase(),
                marketService.getMarketStatusByPeriode(exchangeCode, period)
        );
    }
}
