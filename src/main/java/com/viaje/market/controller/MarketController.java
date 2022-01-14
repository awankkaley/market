package com.viaje.market.controller;

import com.viaje.market.dtos.base_dto.GlobalDto;
import com.viaje.market.dtos.*;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodResultDto;
import com.viaje.market.dtos.hotbit_order.OrderMultipleRequestDto;
import com.viaje.market.dtos.hotbit_order.OrderRequestDto;
import com.viaje.market.dtos.hotbit_order.OrderResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.entities.MarketSetupEntity;
import com.viaje.market.services.MarketServiceImpl;
import com.viaje.market.services.MarketSetupServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
public class MarketController {
    private final MarketServiceImpl marketService;
    private final MarketSetupServiceImpl marketSetupService;

    @GetMapping("/api/v1/setup")
    public GlobalDto<MarketSetupEntity> setup() {
        MarketSetupEntity result = marketSetupService.getSetup().get(0);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @PostMapping("/api/v1/setup")
    public GlobalDto<String> update(@Valid @RequestBody MarketSetupRequestDto requestDto) {
        MarketSetupEntity existing = marketSetupService.getSetup().get(0);
        marketSetupService.updateSetup(requestDto, existing.getId());
        return new GlobalDto<>(
                null,
                "Success"
        );
    }


    @GetMapping("/api/v1/balance/{exchangeCode}")
    public GlobalDto<BalanceResponseDto> balance(@PathVariable String exchangeCode, @RequestHeader("sign") String signature) {
        BalanceResponseDto result = marketService.getBalance(exchangeCode, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/api/v1/market/today/{exchangeCode}")
    public GlobalDto<MarketResponse> marketToday(@PathVariable String exchangeCode, @RequestHeader("sign") String signature) {
        MarketResponse result = marketService.getMarketStatusToday(exchangeCode, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/api/v1/market/period")
    public GlobalDto<HotbitPeriodResultDto> marketPeriod(@RequestParam String period, @RequestHeader("sign") String signature) {
        HotbitPeriodDto result = marketService.getMarketStatusByPeriode("hotbit", Integer.valueOf(period), signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/api/v1/book/order/single/{exchangeCode}")
    public GlobalDto<OrderResponseDto> order(@PathVariable String exchangeCode, @Valid @RequestBody OrderRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.postOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/api/v1/book/order/both/{exchangeCode}")
    public GlobalDto<List<OrderResponseDto>> ordermultiple(@PathVariable String exchangeCode, @Valid @RequestBody OrderMultipleRequestDto orderRequestDto, @RequestHeader("sign") String signature) {
        GlobaExchangeMultipleResponse result = marketService.postMultipleOrder(exchangeCode, orderRequestDto, signature);
        return new GlobalDto<>(
                result.getError(),
                result.getResult()
        );
    }

    @PostMapping("/api/v1/book/cancel")
    public GlobalDto<OrderResponseDto> cancel(@Valid @RequestBody Map<String, Long> orderId, @RequestHeader("sign") String signature) {
        GlobalExchangeResponse result = marketService.cancelOrder(orderId.get("orderId"), signature);
        return new GlobalDto<>(result.getError(), result.getResult());
    }

    @GetMapping("/api/v1/book/all/{page}/{limit}")
    public GlobalDto<List<OrderResponseDto>> getAll(@PathVariable Integer page, @PathVariable Integer limit, @RequestHeader("sign") String signature) {
        List<OrderResponseDto> result = marketService.getAll(page, limit, signature);
        return new GlobalDto<>(null, result);
    }


    @GetMapping("/api/v1/book/by_status/{page}/{limit}/{status}")
    public GlobalDto<List<OrderResponseDto>> getByStatus(@PathVariable @NotNull Integer page, @PathVariable @NotNull Integer limit, @PathVariable @Min(1) @Max(5) Integer status, @RequestHeader("sign") String signature) {
        List<OrderResponseDto> result = marketService.getAllByStatus(page, limit, status, signature);
        return new GlobalDto<>(null, result);
    }

    @GetMapping("/api/v1/book/by_id/{id}")
    public GlobalDto<OrderResponseDto> checkSuccess(@PathVariable Long id, @RequestHeader("sign") String signature) {
        OrderResponseDto result = marketService.getById(id, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/api/v1/book/detail/{id}")
    public GlobalDto<Object> detail(@PathVariable Long id, @RequestHeader("sign") String signature) {
        Object result = marketService.getDetailOrder(id, signature);
        return new GlobalDto<>(
                null,
                result
        );
    }

    @GetMapping("/health_test")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("success");
    }
}
