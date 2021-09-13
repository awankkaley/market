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
            if (result.getError() == null) {
                order.setStatus(ConstantValue.CREATED);
                order.setValid(true);
                if (Objects.equals(orderRequestDto.getSide(), ConstantValue.SIDE_SELL)) {
                    order.setExchangeOrderIdSell(result.getResult().getId());
                    order.setSell(Double.valueOf(result.getResult().getAmount()));
                    order.setSellPrice(Double.valueOf(result.getResult().getPrice()));
                }
                if (Objects.equals(orderRequestDto.getSide(), ConstantValue.SIDE_BUY)) {
                    order.setExchangeOrderIdBuy(result.getResult().getId());
                    order.setBuy(Double.valueOf(result.getResult().getAmount()));
                    order.setBuyPrice(Double.valueOf(result.getResult().getPrice()));
                }
            } else {
                order.setStatus(ConstantValue.FAILED);
                order.setInfo(result.getError().getMessage());
                order.setValid(true);
            }
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(result.getError());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public GlobalExchangeResponse postMultipleOrder(Integer exchange, OrderMultipleRequestDto orderRequestDto, String signature) {
//        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
//        signatureService.isValidSignature(payload, signature);
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        double marketPrice = Double.parseDouble(market.getResult().getLast());
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(exchange, marketPrice));
        OrderEntity result2 = new OrderEntity();
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            HotbitOrderResponseDto resultBuy = hotbitService.postOrder(ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            HotbitOrderResponseDto resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            if (resultBuy == null || resultBuy.getError() != null) {
                updateStatusFailed(order);
                throw new IllegalArgumentException("Failed to create transaction, please try again later");
            }
            if (resultSell == null || resultSell.getError() != null) {
                resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            }
            if (resultSell == null || resultSell.getError() != null) {
                result2 = updateStatusAbnormal(order, resultBuy);
            } else {
                result2 = updateStatusSuccess(order, resultBuy, resultSell);
            }
            return result2.toDto(null);
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
        Long orderExchange = null;
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            if (Objects.equals(order.getSide(), ConstantValue.CREATED)) {
                orderExchange = order.getExchangeOrderIdSell();
            } else {
                orderExchange = order.getExchangeOrderIdBuy();
            }
            HotbitOrderResponseDto result = hotbitService.cancelOrder(orderExchange);
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


    private OrderEntity updateStatusSuccess(OrderEntity order, HotbitOrderResponseDto resultBuy, HotbitOrderResponseDto resultSell) {
        order.setExchangeOrderIdBuy(resultBuy.getResult().getId());
        order.setExchangeOrderIdSell(resultSell.getResult().getId());
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setBuy(Double.valueOf(resultBuy.getResult().getAmount()));
        order.setBuyPrice(Double.valueOf(resultBuy.getResult().getPrice()));
        order.setSell(Double.valueOf(resultSell.getResult().getAmount()));
        order.setSellPrice(Double.valueOf(resultSell.getResult().getPrice()));
        return orderRepository.save(order);
    }

    private OrderEntity updateStatusAbnormal(OrderEntity order, HotbitOrderResponseDto resultBuy) {
        order.setExchangeOrderIdBuy(resultBuy.getResult().getId());
        order.setStatus(ConstantValue.ABNORMAL);
        order.setValid(true);
        order.setBuy(Double.valueOf(resultBuy.getResult().getAmount()));
        order.setBuyPrice(Double.valueOf(resultBuy.getResult().getPrice()));
        return orderRepository.save(order);
    }

    private OrderEntity updateStatusFailed(OrderEntity order) {
        order.setStatus(ConstantValue.FAILED);
        order.setInfo("Failed");
        order.setValid(true);
        return orderRepository.save(order);
    }
}
