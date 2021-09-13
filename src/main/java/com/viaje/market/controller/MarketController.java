package com.viaje.market.controller;

import com.viaje.market.base_dto.GlobalDto;
import com.viaje.market.dto.*;
import com.viaje.market.service.MarketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class MarketController {
    private final MarketService marketService;


    @GetMapping("/balance/{exchangeCode}")
    public GlobalDto<HotbitBalanceResultDto> balance(@Valid @PathVariable Integer exchangeCode, @RequestHeader("sign") String signature) {
        HotbitBalanceDto result = marketService.getBalance(exchangeCode, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/market/today/{exchangeCode}")
    public GlobalDto<HotbitTodayResultDto> marketToday(@Valid @PathVariable Integer exchangeCode, @RequestHeader("sign") String signature) {
        HotbitTodayDto result = marketService.getMarketStatusToday(exchangeCode, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/market/period/{exchangeCode}")
    public GlobalDto<HotbitPeriodResultDto> marketPeriod(@Valid @PathVariable Integer exchangeCode, @RequestParam String period, @RequestHeader("sign") String signature) {
        HotbitPeriodDto result = marketService.getMarketStatusByPeriode(exchangeCode, Integer.valueOf(period), signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @GetMapping("/book/transaction/{exchangeCode}")
    public GlobalDto<HotbitBookResultDto> transaction(@Valid @PathVariable Integer exchangeCode, @RequestParam String side, @RequestParam String offset, @RequestParam String limit, @RequestHeader("sign") String signature) {
        HotbitBookDto result = marketService.getListOfTransaction(exchangeCode, side, offset, limit, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/order/single/{exchangeCode}")
    public GlobalDto<OrderResponseDto> order(@Valid @PathVariable Integer exchangeCode, @RequestBody OrderRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.postOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/order/both/{exchangeCode}")
    public GlobalDto<OrderResponseDto> ordermultiple(@Valid @PathVariable Integer exchangeCode, @RequestBody OrderMultipleRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.postMultipleOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/cancel/{exchangeCode}")
    public GlobalDto<OrderResponseDto> cancel(@Valid @PathVariable Integer exchangeCode, @RequestBody Map<String, Long> orderId, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.cancelOrder(exchangeCode, orderId.get("orderId"), signature);
        return new GlobalDto<>(result.getError(), result.getResult());
    }

    @PostMapping("/book/getall/{page}/{limit}")
    public GlobalDto<List<OrderResponseDto>> getAll(@PathVariable Integer page, @PathVariable Integer limit, @RequestHeader("sign") String signature) {
        List<OrderResponseDto> result = marketService.getAllGlobalOrder(page, limit, signature);
        return new GlobalDto<>(null, result);
    }
}
