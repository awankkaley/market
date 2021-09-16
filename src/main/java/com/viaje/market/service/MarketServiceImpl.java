package com.viaje.market.service;

import com.viaje.market.dto.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dto.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dto.coinsbit_status.CoinsbitStatusDto;
import com.viaje.market.dto.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dto.hotbit_market.HotbitTodayDto;
import com.viaje.market.dto.hotbit_order.HotbitOrderResponseDto;
import com.viaje.market.dto.hotbit_order.OrderMultipleRequestDto;
import com.viaje.market.dto.hotbit_order.OrderRequestDto;
import com.viaje.market.dto.hotbit_order.OrderResponseDto;
import com.viaje.market.dto.hotbit_status.HotbitSuccessResponseDto;
import com.viaje.market.dto.response.BalanceResponseDto;
import com.viaje.market.dto.response.MarketResponse;
import com.viaje.market.entity.OrderEntity;
import com.viaje.market.repository.OrderRepository;
import com.viaje.market.api_key.ApiKeyConfiguration;
import com.viaje.market.dto.*;
import com.viaje.market.util.ConstantValue;
import com.viaje.market.util.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;
    private final CoinsbitService coinsbitService;

    private final SignatureService signatureService;
    private final OrderRepository orderRepository;
    private final ApiKeyConfiguration apiKeyConfiguration;

    @Override
    public BalanceResponseDto getBalance(Integer exchange, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getBalance().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return coinsbitService.getBalance().toResponse();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public MarketResponse getMarketStatusToday(Integer exchange, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusToday().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return coinsbitService.getMarketStatusToday().toResponse();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&period=" + periode;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }


    @Override
    public GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return orderHotbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return orderCoinsbit(orderRequestDto);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }

    }

    @Override
    public GlobaExchangeMultipleResponse postMultipleOrder(Integer exchange, OrderMultipleRequestDto orderRequestDto, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
//        signatureService.isValidSignature(payload, signature);
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        double marketPrice = Double.parseDouble(market.getResult().getLast());
        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(exchange, marketPrice, ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(exchange, marketPrice, ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            HotbitOrderResponseDto resultBuy = hotbitService.postOrder(ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice);
            HotbitOrderResponseDto resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice);
            if (resultBuy == null) {
                orderBuy = updateStatusCreated(orderBuy);
            } else {
                orderBuy = updateStatusSuccess(orderBuy, resultBuy);
            }
            if (resultSell == null) {
                orderSell = updateStatusCreated(orderSell);
            } else {
                orderSell = updateStatusSuccess(orderSell, resultSell);
            }
            OrderResponseDto dataBuy = orderBuy.toDtoList();
            OrderResponseDto dataSell = orderSell.toDtoList();
            return new GlobaExchangeMultipleResponse(null, Arrays.asList(dataBuy, dataSell));
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public GlobalExchangeResponse cancelOrder(Integer exchange, Long orderId, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&orderId=" + orderId;
//        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findByIdAndStatus(orderId, 1).orElseThrow(
                () -> new IllegalArgumentException("Data Not Found")
        );
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            HotbitSuccessResponseDto hotbitSuccessResponseDto = hotbitService.checkSuccessStatus(order.getExchangeOrderId());
            if (hotbitSuccessResponseDto.getResult().getRecords().isEmpty()) {
                HotbitOrderResponseDto result = hotbitService.cancelOrder(order.getExchangeOrderId());
                if (result.getError() == null) {
                    order.setStatus(ConstantValue.FAILED);
                    order.setInfo("Cancel by Viaje");
                    order.setValid(true);
                }
                OrderEntity result2 = orderRepository.save(order);
                return result2.toDto(result.getError());
            } else {
                throw new IllegalArgumentException("Transaction has been settled");
            }

        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public List<OrderResponseDto> getAll(Integer page, Integer limit, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit;
//        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPagination(paging).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit + "&status=" + status;
//        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPaginationAnStatus(paging, status).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDto getById(Long orderId, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
//        signatureService.isValidSignature(payload, signature);
        OrderEntity result = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Data Not Found"));
        return result.toDtoList();
    }

    @Async
    @Override
    public void checkStatusPeriodically() {
        List<OrderEntity> order = orderRepository.findByStatusAndIsValid(1, true);
        if (order.size() != 0) {
            for (OrderEntity orderEntity : order) {
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
                    HotbitSuccessResponseDto hotbitSuccessResponseDto = hotbitService.checkSuccessStatus(orderEntity.getExchangeOrderId());
                    log.error("STATUS PERIODIC : " + hotbitSuccessResponseDto.getResult().getRecords());
                    if (!hotbitSuccessResponseDto.getResult().getRecords().isEmpty()) {
                        orderEntity.setStatus(ConstantValue.SUCCESS);
                        orderRepository.save(orderEntity);
                    }
                }
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_COINSBIT)) {
                    CoinsbitStatusDto coinsbitStatusDto = coinsbitService.checkSuccessStatus(orderEntity.getExchangeOrderId());
                    log.error("STATUS PERIODIC : " + coinsbitStatusDto.getResult().getRecords());
                    if (!coinsbitStatusDto.getResult().getRecords().isEmpty()) {
                        orderEntity.setStatus(ConstantValue.SUCCESS);
                        orderRepository.save(orderEntity);
                    }
                }
            }
        } else {
            log.error("STATUS PERIODIC EMPTY");
        }
    }


    private OrderEntity updateStatusSuccess(OrderEntity order, HotbitOrderResponseDto result) {
        order.setExchangeOrderId(result.getResult().getId());
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setAmount(Double.valueOf(result.getResult().getAmount()));
        order.setPrice(Double.valueOf(result.getResult().getPrice()));
        return orderRepository.save(order);
    }


    private OrderEntity updateStatusCreated(OrderEntity order) {
        order.setExchangeOrderId(null);
        order.setStatus(ConstantValue.CREATED);
        order.setValid(false);
        return orderRepository.save(order);
    }

    private GlobalExchangeResponse orderHotbit(OrderRequestDto orderRequestDto) {
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        double marketPrice = Double.parseDouble(market.getResult().getLast());
        // TODO: 10/09/21 for testing only
//        if (orderRequestDto.getSide() == 1) {
//            marketPrice = marketPrice + 0.1000;
//        }
//        if (orderRequestDto.getSide() == 2) {
//            marketPrice = marketPrice - 0.1000;
//        }
        //<<<FOR TESTING ONLY
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(1, marketPrice));
        HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), marketPrice);
        if (result != null && result.getError() == null) {
            order.setStatus(ConstantValue.CREATED);
            order.setValid(true);
            order.setExchangeOrderId(result.getResult().getId());
            order.setAmount(Double.valueOf(result.getResult().getAmount()));
            order.setPrice(Double.valueOf(result.getResult().getPrice()));
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(null);
        } else {
            HotbitErrorDto error = new HotbitErrorDto(9031, "failed to access hotbit");
            order.setStatus(ConstantValue.FAILED);
            if (result != null) {
                error.setCode(result.getError().getCode());
                error.setMessage(result.getError().getMessage());
            }
            order.setInfo(error.getMessage());
            order.setValid(true);
            orderRepository.save(order);
            throw new IllegalArgumentException(error.getMessage());
        }
    }

    private GlobalExchangeResponse orderCoinsbit(OrderRequestDto orderRequestDto) {
        CoinsbitMarketDto market = coinsbitService.getMarketStatusToday();
        double marketPrice = Double.parseDouble(market.getResult().getLast());
        // TODO: 10/09/21 for testing only
//        if (orderRequestDto.getSide() == 1) {
//            marketPrice = marketPrice + 0.1000;
//        }
//        if (orderRequestDto.getSide() == 2) {
//            marketPrice = marketPrice - 0.1000;
//        }
        //<<<FOR TESTING ONLY
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(2, marketPrice));
        CoinsbitOrderDto result = coinsbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), marketPrice);
        if (result != null && result.isSuccess()) {
            order.setStatus(ConstantValue.CREATED);
            order.setValid(true);
            order.setExchangeOrderId(result.getResult().getOrderId());
            order.setAmount(Double.valueOf(result.getResult().getAmount()));
            order.setPrice(Double.valueOf(result.getResult().getPrice()));
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(null);
        } else {
            HotbitErrorDto error = new HotbitErrorDto(9031, "failed to access coinsbit");
            order.setStatus(ConstantValue.FAILED);
            if (result != null) {
                error.setCode(9031);
                error.setMessage(result.getMessage());
            }
            order.setInfo(error.getMessage());
            order.setValid(true);
            orderRepository.save(order);
            throw new IllegalArgumentException(error.getMessage());
        }
    }
}
