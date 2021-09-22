package com.viaje.market.controller;

import com.viaje.market.base_dto.GlobalDto;
import com.viaje.market.dto.*;
import com.viaje.market.dto.hotbit_balance.HotbitBalanceDto;
import com.viaje.market.dto.hotbit_balance.HotbitBalanceResultDto;
import com.viaje.market.dto.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dto.hotbit_market.HotbitPeriodResultDto;
import com.viaje.market.dto.hotbit_market.HotbitTodayDto;
import com.viaje.market.dto.hotbit_market.HotbitTodayResultDto;
import com.viaje.market.dto.hotbit_order.OrderMultipleRequestDto;
import com.viaje.market.dto.hotbit_order.OrderRequestDto;
import com.viaje.market.dto.hotbit_order.OrderResponseDto;
import com.viaje.market.dto.response.BalanceResponseDto;
import com.viaje.market.dto.response.MarketResponse;
import com.viaje.market.service.MarketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class MarketController {
    private final MarketService marketService;


    @GetMapping("/balance/{exchangeCode}")
    public GlobalDto<BalanceResponseDto> balance(@PathVariable @Min(1) @Max(5) Integer exchangeCode, @RequestHeader("sign") String signature) {
        BalanceResponseDto result = marketService.getBalance(exchangeCode, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/market/today/{exchangeCode}")
    public GlobalDto<MarketResponse> marketToday(@PathVariable @Min(1) @Max(5) Integer exchangeCode, @RequestHeader("sign") String signature) {
        MarketResponse result = marketService.getMarketStatusToday(exchangeCode, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/market/period")
    public GlobalDto<HotbitPeriodResultDto> marketPeriod(@RequestParam String period, @RequestHeader("sign") String signature) {
        HotbitPeriodDto result = marketService.getMarketStatusByPeriode(1, Integer.valueOf(period), signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/order/single/{exchangeCode}")
    public GlobalDto<OrderResponseDto> order(@PathVariable @Min(1) @Max(5) Integer exchangeCode, @Valid @RequestBody OrderRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.postOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/order/both/{exchangeCode}")
    public GlobalDto<List<OrderResponseDto>> ordermultiple(@PathVariable @Min(1) @Max(5) Integer exchangeCode, @Valid @RequestBody OrderMultipleRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobaExchangeMultipleResponse result = marketService.postMultipleOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/book/cancel/{exchangeCode}")
    public GlobalDto<OrderResponseDto> cancel(@PathVariable @Min(1) @Max(5) Integer exchangeCode, @Valid @RequestBody Map<String, Long> orderId, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.cancelOrder(exchangeCode, orderId.get("orderId"), signature);
        return new GlobalDto<>(result.getError(), result.getResult());
    }

    @GetMapping("/book/all/{page}/{limit}")
    public GlobalDto<List<OrderResponseDto>> getAll(@PathVariable Integer page, @PathVariable Integer limit, @RequestHeader("sign") String signature) {
        List<OrderResponseDto> result = marketService.getAll(page, limit, signature);
        return new GlobalDto<>(null, result);
    }


    @GetMapping("/book/by_status/{page}/{limit}/{status}")
    public GlobalDto<List<OrderResponseDto>> getByStatus(@PathVariable @NotNull Integer page, @PathVariable @NotNull Integer limit, @PathVariable @Min(1) @Max(5) Integer status, @RequestHeader("sign") String signature) {
        List<OrderResponseDto> result = marketService.getAllByStatus(page, limit, status, signature);
        return new GlobalDto<>(null, result);
    }

    @GetMapping("/book/by_id/{id}")
    public GlobalDto<OrderResponseDto> checkSuccess(@PathVariable Long id, @RequestHeader("sign") String signature) {
        OrderResponseDto result = marketService.getById(id, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/book/detail/{id}")
    public GlobalDto<Object> detail(@PathVariable Long id, @RequestHeader("sign") String signature) {
        Object result = marketService.getDetailOrder(id, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }
}
