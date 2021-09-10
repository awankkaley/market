package com.viaje.market.service;

import com.viaje.market.entity.OrderEntity;
import com.viaje.market.repository.OrderRepository;
import com.viaje.market.api_key.ApiKeyConfiguration;
import com.viaje.market.dto.*;
import com.viaje.market.util.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getBalance();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitTodayDto getMarketStatusToday(Integer exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getMarketStatusToday();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitPeriodDto getMarketStatusByPeriode(Integer exchange, Integer periode, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&period=" + periode;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public HotbitBookDto getListOfTransaction(Integer exchange, String side, String offset, String limit, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + side + "&offset=" + offset + "&limit=" + limit;
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
        signatureService.isValidSignature(payload, signature);
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
        if (exchange == 1) {
            HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), marketPrice, orderRequestDto.getIsfee());
            if (result.getError() == null) {
                order.setStatus(1);
                order.setValid(true);
                if (orderRequestDto.getSide() == 1) {
                    order.setExchangeOrderIdSell(result.getResult().getId());
                    order.setSell(Double.valueOf(result.getResult().getAmount()));
                    order.setSellPrice(Double.valueOf(result.getResult().getPrice()));
                }
                if (orderRequestDto.getSide() == 2) {
                    order.setExchangeOrderIdBuy(result.getResult().getId());
                    order.setBuy(Double.valueOf(result.getResult().getAmount()));
                    order.setBuyPrice(Double.valueOf(result.getResult().getPrice()));
                }
            } else {
                order.setStatus(2);
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
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&amount=" + orderRequestDto.getAmount().toString() + "&isfee=" + orderRequestDto.getIsfee();
        signatureService.isValidSignature(payload, signature);
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        double marketPrice = Double.parseDouble(market.getResult().getLast());
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(exchange, marketPrice));
        if (exchange == 1) {
            HotbitOrderResponseDto resultBuy = hotbitService.postOrder(2, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            HotbitOrderResponseDto resultSell = hotbitService.postOrder(1, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), marketPrice, orderRequestDto.getIsfee());
            if (resultSell.getError() == null && resultBuy.getError() == null) {
                order.setExchangeOrderIdBuy(resultBuy.getResult().getId());
                order.setExchangeOrderIdSell(resultSell.getResult().getId());
                order.setStatus(1);
                order.setValid(true);
                order.setBuy(Double.valueOf(resultBuy.getResult().getAmount()));
                order.setBuyPrice(Double.valueOf(resultBuy.getResult().getPrice()));
                order.setSell(Double.valueOf(resultSell.getResult().getAmount()));
                order.setSellPrice(Double.valueOf(resultSell.getResult().getPrice()));
            } else {
                order.setStatus(2);
                order.setInfo(resultBuy.getError().getMessage() + resultSell.getError().getMessage());
                order.setValid(true);
            }
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(resultSell.getError());
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
        if (exchange == 1) {
            if (order.getSide() == 1) {
                orderExchange = order.getExchangeOrderIdSell();
            } else {
                orderExchange = order.getExchangeOrderIdBuy();
            }
            HotbitOrderResponseDto result = hotbitService.cancelOrder(orderExchange);
            if (result.getError() == null) {
                order.setStatus(2);
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
}
