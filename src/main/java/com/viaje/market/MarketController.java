package com.viaje.market;

import com.viaje.market.base_dto.GlobalDto;
import com.viaje.market.dto.*;
import com.viaje.market.service.MarketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class MarketController {
    private final MarketService marketService;

    @GetMapping("/balance/{exchangeCode}")
    public GlobalDto<HotbitBalanceResultDto> balance(@Valid @PathVariable Integer exchangeCode) {
        HotbitBalanceDto result = marketService.getBalance(exchangeCode);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/market/today/{exchangeCode}")
    public GlobalDto<HotbitTodayResultDto> marketToday(@Valid @PathVariable Integer exchangeCode) {
        HotbitTodayDto result = marketService.getMarketStatusToday(exchangeCode);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/market/period/{exchangeCode}")
    public GlobalDto<HotbitPeriodResultDto> marketPeriod(@Valid @PathVariable Integer exchangeCode, @RequestParam String period) {
        HotbitPeriodDto result = marketService.getMarketStatusByPeriode(exchangeCode, Integer.valueOf(period));
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/book/transaction/{exchangeCode}")
    public GlobalDto<HotbitBookResultDto> transaction(@Valid @PathVariable Integer exchangeCode, @RequestParam String side, @RequestParam String offset, @RequestParam String limit) {
        HotbitBookDto result = marketService.getListOfTransaction(exchangeCode, side, offset, limit);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/order/{exchangeCode}")
    public GlobalDto<HotbitOrderResultDto> order(@Valid @PathVariable Integer exchangeCode, @RequestBody OrderRequestDto orderRequestDto) {
        HotbitOrderResponseDto result = marketService.postOrder(exchangeCode, orderRequestDto);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/cancel/{exchangeCode}")
    public GlobalDto<HotbitOrderResultDto> cancel(@Valid @PathVariable Integer exchangeCode, @RequestBody Map<String, Long> orderId) {
        HotbitOrderResponseDto result = marketService.cancelOrder(exchangeCode, orderId.get("orderId"));
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }
}
