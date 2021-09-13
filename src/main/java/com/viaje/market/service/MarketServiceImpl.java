package com.viaje.market.service;

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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final HotbitService hotbitService;
    private final SignatureService signatureService;
    private final OrderRepository orderRepository;
    private final ApiKeyConfiguration apiKeyConfiguration;

    @Override
    public HotbitBalanceDto getBalance(Integer exchange, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getBalance();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitTodayDto getMarketStatusToday(Integer exchange, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusToday();
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
    public HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + side + "&offset=" + offset + "&limit=" + limit;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
//        signatureService.isValidSignature(payload, signature);
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
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(exchange, marketPrice));
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), marketPrice, orderRequestDto.getIsfee());
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
            HotbitOrderResponseDto resultBuy = hotbitService.postOrder(ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            HotbitOrderResponseDto resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
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
        // TODO: 10/09/21 Check If Order Success
        OrderEntity order = orderRepository.findByIdAndStatus(orderId, 1).orElseThrow(
                () -> new IllegalArgumentException("Data Not Found")
        );
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            HotbitOrderResponseDto result = hotbitService.cancelOrder(order.getExchangeOrderId());
            if (result.getError() == null) {
                order.setStatus(ConstantValue.FAILED);
                order.setInfo("Cancel by Viaje");
                order.setValid(true);
            }
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(result.getError());

        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public List<OrderResponseDto> getAllGlobalOrder(Integer page, Integer limit, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit;
//        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPagination(paging).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }

    @Override
    public HotbitSuccessResponseDto checkSuccessStatus(Integer exchange, Long orderId, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&orderId=" + orderId;
//        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.checkSuccessStatus(orderId);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public void checkStatusPeriodically() {
        List<OrderEntity> order = orderRepository.findAll();
        if (order.size() != 0) {
            List<Long> orderIdBuy = order.stream().map(OrderEntity::getExchangeOrderId).collect(Collectors.toList());
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
}
