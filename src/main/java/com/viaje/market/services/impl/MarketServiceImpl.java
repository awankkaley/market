package com.viaje.market.services.impl;

import com.viaje.market.dtos.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderResultDto;
import com.viaje.market.dtos.coinsbit_status.CoinsbitStatusDto;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dtos.hotbit_market.HotbitTodayDto;
import com.viaje.market.dtos.hotbit_order.*;
import com.viaje.market.dtos.hotbit_status.HotbitSuccessResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.entities.CoinsbitEntity;
import com.viaje.market.entities.HotbitEntity;
import com.viaje.market.entities.OrderEntity;
import com.viaje.market.repositories.CoinbitRepository;
import com.viaje.market.repositories.HotbitRepository;
import com.viaje.market.repositories.OrderRepository;
import com.viaje.market.config.api_key.ApiKeyConfiguration;
import com.viaje.market.dtos.*;
import com.viaje.market.services.CoinsbitService;
import com.viaje.market.services.HotbitService;
import com.viaje.market.services.MarketService;
import com.viaje.market.services.SignatureService;
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
    private final HotbitRepository hotbitRepository;
    private final CoinbitRepository coinbitRepository;
    private final SignatureService signatureService;
    private final OrderRepository orderRepository;
    private final ApiKeyConfiguration apiKeyConfiguration;

    @Override
    public BalanceResponseDto getBalance(String exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
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
    public MarketResponse getMarketStatusToday(String exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
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
    public HotbitPeriodDto getMarketStatusByPeriode(String exchange, Integer periode, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&period=" + periode;
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }


    @Override
    public GlobalExchangeResponse postOrder(String exchange, OrderRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString();
        signatureService.isValidSignature(payload, signature);
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
    public GlobaExchangeMultipleResponse postMultipleOrder(String exchange, OrderMultipleRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&amount=" + orderRequestDto.getAmount().toString();
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return postMultipleOrderHotbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return postMultipleOrderCoinsbit(orderRequestDto);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }

    }

    @Override
    public GlobalExchangeResponse cancelOrder(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findByIdAndStatus(orderId, 1).orElseThrow(
                () -> new IllegalArgumentException("Data Not Found")
        );
        if (order.getExchangeCode().equals("hotbit")) {
            return cancelOrderHotsbit(order);
        } else {
            return cancelOrderCoinsbit(order);
        }
    }

    @Override
    public List<OrderResponseDto> getAll(Integer page, Integer limit, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit;
        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPagination(paging).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit + "&status=" + status;
        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPaginationAnStatus(paging, status).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDto getById(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity result = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Data Not Found"));
        return result.toDtoList();
    }

    @Override
    public Object getDetailOrder(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Data Not Found"));
        if (Objects.equals(order.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
            return getDetailOrderHotbit(order.getExchangeOrderId());
        }
        if (Objects.equals(order.getExchangeCode(), ConstantValue.EXCHANGE_COINSBIT)) {
            return getDetailOrderCoinsbit(order.getExchangeOrderId());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
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
                    if (coinsbitStatusDto.getResult() != null) {
                        log.error("STATUS PERIODIC : " + coinsbitStatusDto.getResult().getRecords().toString());
                        if (!coinsbitStatusDto.getResult().getRecords().isEmpty()) {
                            orderEntity.setStatus(ConstantValue.SUCCESS);
                            orderRepository.save(orderEntity);
                        }
                    }
                }
            }
        }
    }

    @Async
    @Override
    public void createPendingOrder() {
        List<OrderEntity> order = orderRepository.findByStatusAndIsValid(1, false);
        if (order.size() != 0) {
            for (OrderEntity orderEntity : order) {
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
                    HotbitOrderResponseDto result = hotbitService.postOrder(orderEntity.getSide(), orderEntity.getAmount(), orderEntity.getCurrentPrice());
                    if (result != null && result.getError() == null) {
                        orderEntity.setStatus(ConstantValue.CREATED);
                        orderEntity.setValid(true);
                        orderEntity.setExchangeOrderId(result.getResult().getId());
                        orderEntity.setAmount(Double.valueOf(result.getResult().getAmount()));
                        orderEntity.setPrice(Double.valueOf(result.getResult().getPrice()));
                        orderRepository.save(orderEntity);
                    }
                }
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_COINSBIT)) {
                    CoinsbitOrderDto result = coinsbitService.postOrder(orderEntity.getSide(), orderEntity.getAmount(), orderEntity.getCurrentPrice());
                    if (result != null && result.isSuccess()) {
                        orderEntity.setStatus(ConstantValue.CREATED);
                        orderEntity.setValid(true);
                        orderEntity.setExchangeOrderId(result.getResult().getOrderId());
                        orderEntity.setAmount(Double.valueOf(result.getResult().getAmount()));
                        orderEntity.setPrice(Double.valueOf(result.getResult().getPrice()));
                        orderRepository.save(orderEntity);
                    }
                }
            }
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

    private OrderEntity updateStatusFailed(OrderEntity order) {
        order.setStatus(ConstantValue.FAILED);
        order.setValid(true);
        return orderRepository.save(order);
    }

    private OrderEntity updateStatusSuccessCoinsbit(OrderEntity order, CoinsbitOrderDto result) {
        order.setExchangeOrderId(result.getResult().getOrderId());
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setAmount(Double.valueOf(result.getResult().getAmount()));
        order.setPrice(Double.valueOf(result.getResult().getPrice()));
        return orderRepository.save(order);
    }


    private OrderEntity updateStatusPending(OrderEntity order) {
        order.setExchangeOrderId(null);
        order.setStatus(ConstantValue.CREATED);
        order.setValid(false);
        return orderRepository.save(order);
    }

    private GlobalExchangeResponse orderHotbit(OrderRequestDto orderRequestDto) {
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(Double.parseDouble(market.getResult().getLast()));
        }
        // TODO: 10/09/21 for testing only
//        if (orderRequestDto.getSide() == 1) {
//            marketPrice = marketPrice + 0.1000;
//        }
//        if (orderRequestDto.getSide() == 2) {
//            marketPrice = marketPrice - 0.1000;
//        }
        //<<<FOR TESTING ONLY
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity("hotbit", orderRequestDto.getPrice()));
        HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice());
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
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(Double.parseDouble(market.getResult().getLast()));
        }
        // TODO: 10/09/21 for testing only
//        if (orderRequestDto.getSide() == 1) {
//            marketPrice = marketPrice + 0.1000;
//        }
//        if (orderRequestDto.getSide() == 2) {
//            marketPrice = marketPrice - 0.1000;
//        }
        //<<<FOR TESTING ONLY
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity("coinsbit", orderRequestDto.getPrice()));
        CoinsbitOrderDto result = coinsbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice());
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

    private GlobalExchangeResponse cancelOrderHotsbit(OrderEntity order) {
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
    }

    private GlobalExchangeResponse cancelOrderCoinsbit(OrderEntity order) {
        CoinsbitStatusDto coinsbitStatusDto = coinsbitService.checkSuccessStatus(order.getExchangeOrderId());

        if (coinsbitStatusDto.isSuccess()) {
            if (coinsbitStatusDto.getResult() == null) {
                CoinsbitOrderDto result = coinsbitService.cancelOrder(order.getExchangeOrderId());
                if (result.isSuccess()) {
                    order.setStatus(ConstantValue.FAILED);
                    order.setInfo("Cancel by Viaje");
                    order.setValid(true);
                }
                OrderEntity result2 = orderRepository.save(order);
                return result2.toDto(null);
            } else {
                throw new IllegalArgumentException("Transaction has been settled");
            }
        } else {
            throw new IllegalArgumentException("Failed to Access Coinsbit");
        }

    }

    private GlobaExchangeMultipleResponse postMultipleOrderHotbit(OrderMultipleRequestDto orderRequestDto) {
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(Double.parseDouble(market.getResult().getLast()));
        }
        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_HOTBIT, orderRequestDto.getPrice(), ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_HOTBIT, orderRequestDto.getPrice(), ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        HotbitOrderResponseDto resultBuy = hotbitService.postOrder(ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), orderRequestDto.getPrice());
        if (resultBuy == null) {
            updateStatusFailed(orderBuy);
            updateStatusFailed(orderSell);
            throw new IllegalArgumentException("Failed to access Hotbit");
        } else {
            orderBuy = updateStatusSuccess(orderBuy, resultBuy);
        }
        HotbitOrderResponseDto resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), orderRequestDto.getPrice());
        if (resultSell == null) {
            orderSell = updateStatusPending(orderSell);
        } else {
            orderSell = updateStatusSuccess(orderSell, resultSell);
        }
        OrderResponseDto dataBuy = orderBuy.toDtoList();
        OrderResponseDto dataSell = orderSell.toDtoList();
        return new GlobaExchangeMultipleResponse(null, Arrays.asList(dataBuy, dataSell));
    }

    private GlobaExchangeMultipleResponse postMultipleOrderCoinsbit(OrderMultipleRequestDto orderRequestDto) {
        CoinsbitMarketDto market = coinsbitService.getMarketStatusToday();
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(Double.parseDouble(market.getResult().getLast()));
        }
        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_COINSBIT, orderRequestDto.getPrice(), ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_COINSBIT, orderRequestDto.getPrice(), ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount())));
        CoinsbitOrderDto resultBuy = coinsbitService.postOrder(ConstantValue.SIDE_BUY, Util.getAmountBuyFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), orderRequestDto.getPrice());
        if (resultBuy == null) {
            updateStatusFailed(orderBuy);
            updateStatusFailed(orderSell);
            throw new IllegalArgumentException("Failed to access Hotbit");
        } else {
            orderBuy = updateStatusSuccessCoinsbit(orderBuy, resultBuy);
        }
        CoinsbitOrderDto resultSell = coinsbitService.postOrder(ConstantValue.SIDE_SELL, Util.getAmountSellFromPercentage(orderRequestDto.getBuyPercent(), orderRequestDto.getAmount()), orderRequestDto.getPrice());
        if (resultSell == null) {
            orderSell = updateStatusPending(orderSell);
        } else {
            orderSell = updateStatusSuccessCoinsbit(orderSell, resultSell);
        }
        OrderResponseDto dataBuy = orderBuy.toDtoList();
        OrderResponseDto dataSell = orderSell.toDtoList();
        return new GlobaExchangeMultipleResponse(null, Arrays.asList(dataBuy, dataSell));
    }

    private CoinsbitOrderResultDto getDetailOrderCoinsbit(Long orderId) {
        CoinsbitEntity coinsbit = coinbitRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Detail Order Not Found"));
        try {
            return coinsbit.toDto();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private HotbitOrderResultDto getDetailOrderHotbit(Long orderId) {
        HotbitEntity hotbit = hotbitRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Detail Order Not Found"));
        try {
            return hotbit.toDto();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
