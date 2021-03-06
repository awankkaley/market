package com.viaje.market.services;

import com.viaje.market.dtos.coinsbit_market.CoinsbitMarketDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderDto;
import com.viaje.market.dtos.coinsbit_order.CoinsbitOrderResultDto;
import com.viaje.market.dtos.coinsbit_status.CoinsbitStatusDto;
import com.viaje.market.dtos.digifinex.*;
import com.viaje.market.dtos.hotbit_market.HotbitPeriodDto;
import com.viaje.market.dtos.hotbit_market.HotbitTodayDto;
import com.viaje.market.dtos.hotbit_order.*;
import com.viaje.market.dtos.hotbit_status.HotbitSuccessResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.entities.*;
import com.viaje.market.repositories.CoinbitRepository;
import com.viaje.market.repositories.DigifinexRepository;
import com.viaje.market.repositories.HotbitRepository;
import com.viaje.market.repositories.OrderRepository;
import com.viaje.market.config.api_key.ApiKeyConfiguration;
import com.viaje.market.dtos.*;
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
public class MarketServiceImpl {
    private final HotbitServiceImpl hotbitService;
    private final CoinsbitServiceImpl coinsbitService;
    private final DigifinexServiceImpl digifinexService;
    private final HotbitRepository hotbitRepository;
    private final CoinbitRepository coinbitRepository;
    private final DigifinexRepository digifinexRepository;
    private final SignatureValidator signatureService;
    private final OrderRepository orderRepository;
    private final ApiKeyConfiguration apiKeyConfiguration;
    private final MarketSetupServiceImpl marketSetupService;

    public BalanceResponseDto getBalance(String exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getBalance().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return coinsbitService.getBalance().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_DIGIFINEX)) {
            return digifinexService.getBalance().toResponse();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    public MarketResponse getMarketStatusToday(String exchange, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusToday().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return coinsbitService.getMarketStatusToday().toResponse();
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_DIGIFINEX)) {
            return digifinexService.getMarketStatus().toResponse();
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    public HotbitPeriodDto getMarketStatusByPeriode(String exchange, Integer periode, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&period=" + periode;
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return hotbitService.getMarketStatusByPeriode(periode);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }


    public GlobalExchangeResponse postOrder(String exchange, OrderRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString();
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return orderHotbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return orderCoinsbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_DIGIFINEX)) {
            return orderDigifinex(orderRequestDto);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }

    }

    public GlobaExchangeMultipleResponse postMultipleOrder(String exchange, OrderMultipleRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange;
        signatureService.isValidSignature(payload, signature);
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_HOTBIT)) {
            return postMultipleOrderHotbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_COINSBIT)) {
            return postMultipleOrderCoinsbit(orderRequestDto);
        }
        if (Objects.equals(exchange, ConstantValue.EXCHANGE_DIGIFINEX)) {
            return postMultipleOrderDigifinex(orderRequestDto);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }

    }

    public GlobalExchangeResponse cancelOrder(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findByIdAndStatus(orderId, 1).orElseThrow(
                () -> new IllegalArgumentException("Data Not Found")
        );
        if (order.getExchangeCode().equals("hotbit")) {
            return cancelOrderHotsbit(order);
        }
        if (order.getExchangeCode().equals("digifinex")) {
            return cancelOrderDigifinex(order);
        } else {
            return cancelOrderCoinsbit(order);
        }
    }

    public List<OrderResponseDto> getAll(Integer page, Integer limit, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit;
        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPagination(paging).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getAllByStatus(Integer page, Integer limit, Integer status, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit + "&status=" + status;
        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPaginationAnStatus(paging, status).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }


    public OrderResponseDto getById(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity result = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Data Not Found"));
        return result.toDtoList();
    }

    public Object getDetailOrder(Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Data Not Found"));
        if (Objects.equals(order.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
            return getDetailOrderHotbit(Long.valueOf(order.getExchangeOrderId()));
        }
        if (Objects.equals(order.getExchangeCode(), ConstantValue.EXCHANGE_COINSBIT)) {
            return getDetailOrderCoinsbit(Long.valueOf(order.getExchangeOrderId()));
        }
        if (Objects.equals(order.getExchangeCode(), ConstantValue.EXCHANGE_DIGIFINEX)) {
            return getDetailOrderDigifinex(order.getExchangeOrderId());
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }


    @Async
    public void checkStatusPeriodically() {
        List<OrderEntity> order = orderRepository.findByStatusAndIsValid(1, true);
        if (order.size() != 0) {
            for (OrderEntity orderEntity : order) {
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
                    HotbitSuccessResponseDto hotbitSuccessResponseDto = hotbitService.checkSuccessStatus(Long.valueOf(orderEntity.getExchangeOrderId()));
                    log.error("STATUS PERIODIC : " + hotbitSuccessResponseDto.getResult().getRecords());
                    if (!hotbitSuccessResponseDto.getResult().getRecords().isEmpty()) {
                        orderEntity.setStatus(ConstantValue.SUCCESS);
                        orderRepository.save(orderEntity);
                    }
                }
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_COINSBIT)) {
                    CoinsbitStatusDto coinsbitStatusDto = coinsbitService.checkSuccessStatus(Long.valueOf(orderEntity.getExchangeOrderId()));
                    if (coinsbitStatusDto.getResult() != null) {
                        log.error("STATUS PERIODIC : " + coinsbitStatusDto.getResult().getRecords().toString());
                        if (!coinsbitStatusDto.getResult().getRecords().isEmpty()) {
                            orderEntity.setStatus(ConstantValue.SUCCESS);
                            orderRepository.save(orderEntity);
                        }
                    }
                }
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_DIGIFINEX)) {
                    DigifinexStatusResponse digifinexStatusResponse = digifinexService.getStatus(orderEntity.getExchangeOrderId());
                    if (digifinexStatusResponse.getCode() == 0) {
                        log.error("STATUS PERIODIC : " + digifinexStatusResponse.getData().get(0));
                        if (digifinexStatusResponse.getData().get(0).getStatus() == 2 || digifinexStatusResponse.getData().get(0).getStatus() == 1) {
                            orderEntity.setStatus(ConstantValue.SUCCESS);
                            orderRepository.save(orderEntity);
                        }
                    }
                }
            }
        }
    }

    @Async
    public void createPendingOrder() {
        List<OrderEntity> order = orderRepository.findByStatusAndIsValid(1, false);
        if (order.size() != 0) {
            for (OrderEntity orderEntity : order) {
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_HOTBIT)) {
                    HotbitOrderResponseDto result = hotbitService.postOrder(orderEntity.getSide(), orderEntity.getAmount(), orderEntity.getCurrentPrice());
                    if (result != null && result.getError() == null) {
                        orderEntity.setStatus(ConstantValue.CREATED);
                        orderEntity.setValid(true);
                        orderEntity.setExchangeOrderId(String.valueOf(result.getResult().getId()));
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
                        orderEntity.setExchangeOrderId(String.valueOf(result.getResult().getOrderId()));
                        orderEntity.setAmount(Double.valueOf(result.getResult().getAmount()));
                        orderEntity.setPrice(Double.valueOf(result.getResult().getPrice()));
                        orderRepository.save(orderEntity);
                    }
                }
                if (Objects.equals(orderEntity.getExchangeCode(), ConstantValue.EXCHANGE_DIGIFINEX)) {
                    DigifinexStatusResponse result = digifinexService.postOrder(orderEntity.getSide(), orderEntity.getAmount(), orderEntity.getCurrentPrice());
                    if (result.getCode() == 0 && !result.getData().isEmpty()) {
                        orderEntity.setStatus(ConstantValue.CREATED);
                        orderEntity.setValid(true);
                        orderEntity.setExchangeOrderId(String.valueOf(result.getData().get(0).getOrder_id()));
                        orderEntity.setAmount(result.getData().get(0).getAmount());
                        orderEntity.setPrice(result.getData().get(0).getPrice());
                        orderRepository.save(orderEntity);
                    }
                }
            }
        }
    }


    private OrderEntity updateStatusSuccess(OrderEntity order, HotbitOrderResponseDto result) {
        order.setExchangeOrderId(String.valueOf(result.getResult().getId()));
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setAmount(Double.valueOf(result.getResult().getAmount()));
        order.setPrice(Double.valueOf(result.getResult().getPrice()));
        return orderRepository.save(order);
    }

    private void updateStatusFailed(OrderEntity order) {
        order.setStatus(ConstantValue.FAILED);
        order.setValid(true);
        orderRepository.save(order);
    }

    private OrderEntity updateStatusSuccessCoinsbit(OrderEntity order, CoinsbitOrderDto result) {
        order.setExchangeOrderId(String.valueOf(result.getResult().getOrderId()));
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setAmount(Double.valueOf(result.getResult().getAmount()));
        order.setPrice(Double.valueOf(result.getResult().getPrice()));
        return orderRepository.save(order);
    }

    private OrderEntity updateStatusSuccessDigifinex(OrderEntity order, DigifinexStatusDataItem result) {
        order.setExchangeOrderId(String.valueOf(result.getOrder_id()));
        order.setStatus(ConstantValue.CREATED);
        order.setValid(true);
        order.setAmount(result.getAmount());
        order.setPrice(result.getPrice());
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
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity("hotbit", orderRequestDto.getPrice()));
        HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice());
        if (result != null && result.getError() == null) {
            order.setStatus(ConstantValue.CREATED);
            order.setValid(true);
            order.setExchangeOrderId(String.valueOf(result.getResult().getId()));
            order.setAmount(Double.valueOf(result.getResult().getAmount()));
            order.setPrice(Double.valueOf(result.getResult().getPrice()));
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(null);
        } else {
            order.setStatus(ConstantValue.FAILED);
            order.setInfo("Failed to access hotbit");
            order.setValid(true);
            orderRepository.save(order);
            throw new IllegalArgumentException("Failed to access hotbit");
        }
    }

    private GlobalExchangeResponse orderCoinsbit(OrderRequestDto orderRequestDto) {
        CoinsbitMarketDto market = coinsbitService.getMarketStatusToday();
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(Double.parseDouble(market.getResult().getLast()));
        }
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity("coinsbit", orderRequestDto.getPrice()));
        CoinsbitOrderDto result = coinsbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice());
        if (result != null && result.isSuccess()) {
            order.setStatus(ConstantValue.CREATED);
            order.setValid(true);
            order.setExchangeOrderId(String.valueOf(result.getResult().getOrderId()));
            order.setAmount(Double.valueOf(result.getResult().getAmount()));
            order.setPrice(Double.valueOf(result.getResult().getPrice()));
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(null);
        } else {
            order.setStatus(ConstantValue.FAILED);
            order.setInfo("Failed to access hotbit");
            order.setValid(true);
            orderRepository.save(order);
            throw new IllegalArgumentException("Failed to access hotbit");
        }
    }


    private GlobalExchangeResponse orderDigifinex(OrderRequestDto orderRequestDto) {
        DigifinexMarketResponse market = digifinexService.getMarketStatus();
        if (orderRequestDto.getPrice() == null) {
            orderRequestDto.setPrice(market.getTicker().get(0).getLast());
        }
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity("digifinex", orderRequestDto.getPrice()));
        DigifinexStatusResponse result = digifinexService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice());
        if (result.getCode() == 0 && !result.getData().isEmpty()) {
            order.setStatus(ConstantValue.CREATED);
            order.setValid(true);
            order.setExchangeOrderId(result.getData().get(0).getOrder_id());
            order.setAmount(result.getData().get(0).getAmount());
            order.setPrice(result.getData().get(0).getPrice());
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(null);
        } else {
            order.setStatus(ConstantValue.FAILED);
            order.setInfo("Failed to access digifinex");
            order.setValid(true);
            orderRepository.save(order);
            throw new IllegalArgumentException("Failed to access digifinex");
        }
    }

    private GlobalExchangeResponse cancelOrderHotsbit(OrderEntity order) {
        HotbitSuccessResponseDto hotbitSuccessResponseDto = hotbitService.checkSuccessStatus(Long.valueOf(order.getExchangeOrderId()));
        if (hotbitSuccessResponseDto.getResult().getRecords().isEmpty()) {
            HotbitOrderResponseDto result = hotbitService.cancelOrder(Long.valueOf(order.getExchangeOrderId()));
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
        CoinsbitStatusDto coinsbitStatusDto = coinsbitService.checkSuccessStatus(Long.valueOf(order.getExchangeOrderId()));

        if (coinsbitStatusDto.isSuccess()) {
            if (coinsbitStatusDto.getResult() == null) {
                CoinsbitOrderDto result = coinsbitService.cancelOrder(Long.valueOf(order.getExchangeOrderId()));
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


    private GlobalExchangeResponse cancelOrderDigifinex(OrderEntity order) {
        DigifinexStatusResponse digifinexStatusResponse = digifinexService.getStatus(order.getExchangeOrderId());

        if (!digifinexStatusResponse.getData().isEmpty()) {
            if (digifinexStatusResponse.getData().get(0).getStatus() != 3) {
                DigifinexCancelResponse cancel = digifinexService.cancelOrder(order.getExchangeOrderId());
                if (!cancel.getSuccess().isEmpty()) {
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
            throw new IllegalArgumentException("Failed to Access Digifinex");
        }

    }

    private GlobaExchangeMultipleResponse postMultipleOrderHotbit(OrderMultipleRequestDto orderRequestDto) {
        MarketSetupEntity marketSetupEntity = marketSetupService.getSetup().get(0);
        HotbitTodayDto market = hotbitService.getMarketStatusToday();
        log.info("Buy Current price : " + market.getResult().getLast());

        Double buyUsdAmount = Util.getBuyAmountByRation(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Buy USD Amount : " + buyUsdAmount);

        Double buyBsiAmount = Util.calculatetoBsiAmount(Double.valueOf(market.getResult().getLast()), buyUsdAmount, 2);
        log.info("Buy BSI Amount : " + buyBsiAmount);

        Double profitPrice = Util.calculateProfitPrice(Double.valueOf(market.getResult().getLast()), marketSetupEntity.getProfitPercent(), 4);
        log.info("Sell Profit Price : " + profitPrice);

        Double sellUsdAmount = Util.getSellAmountFromRatio(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Sell USD Amount : " + sellUsdAmount);

        Double sellBsiAmount = Util.calculatetoBsiAmount(profitPrice, sellUsdAmount, 2);
        log.info("Sell BSI Amount : " + sellBsiAmount);

        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_HOTBIT, Double.valueOf(market.getResult().getLast()), ConstantValue.SIDE_BUY, buyBsiAmount));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_HOTBIT, profitPrice, ConstantValue.SIDE_SELL, sellBsiAmount));
        HotbitOrderResponseDto resultBuy = hotbitService.postOrder(ConstantValue.SIDE_BUY, buyBsiAmount, Double.valueOf(market.getResult().getLast()));
        if (resultBuy == null) {
            updateStatusFailed(orderBuy);
            updateStatusFailed(orderSell);
            throw new IllegalArgumentException("Failed to access Hotbit");
        } else {
            orderBuy = updateStatusSuccess(orderBuy, resultBuy);
        }
        HotbitOrderResponseDto resultSell = hotbitService.postOrder(ConstantValue.SIDE_SELL, sellBsiAmount, profitPrice);
        if (resultSell.getResult() == null) {
            orderSell = updateStatusPending(orderSell);
        } else {
            orderSell = updateStatusSuccess(orderSell, resultSell);
        }
        OrderResponseDto dataBuy = orderBuy.toDtoList();
        OrderResponseDto dataSell = orderSell.toDtoList();

        return new GlobaExchangeMultipleResponse(null, Arrays.asList(dataBuy, dataSell));
    }

    private GlobaExchangeMultipleResponse postMultipleOrderCoinsbit(OrderMultipleRequestDto orderRequestDto) {
        MarketSetupEntity marketSetupEntity = marketSetupService.getSetup().get(0);
        CoinsbitMarketDto market = coinsbitService.getMarketStatusToday();
        log.info("Buy Current price : " + market.getResult().getLast());

        Double buyUsdAmount = Util.getBuyAmountByRation(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Buy USD Amount : " + buyUsdAmount);

        Double buyBsiAmount = Util.calculatetoBsiAmount(Double.valueOf(market.getResult().getLast()), buyUsdAmount, 6);
        log.info("Buy BSI Amount : " + buyBsiAmount);

        Double profitPrice = Util.calculateProfitPrice(Double.valueOf(market.getResult().getLast()), marketSetupEntity.getProfitPercent(), 4);
        log.info("Sell Profit Price : " + profitPrice);

        Double sellUsdAmount = Util.getSellAmountFromRatio(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Sell USD Amount : " + sellUsdAmount);

        Double sellBsiAmount = Util.calculatetoBsiAmount(profitPrice, sellUsdAmount, 6);
        log.info("Sell BSI Amount : " + sellBsiAmount);

        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_COINSBIT, Double.valueOf(market.getResult().getLast()), ConstantValue.SIDE_BUY, buyBsiAmount));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_COINSBIT, profitPrice, ConstantValue.SIDE_SELL, sellBsiAmount));
        CoinsbitOrderDto resultBuy = coinsbitService.postOrder(ConstantValue.SIDE_BUY, buyBsiAmount, Double.valueOf(market.getResult().getLast()));
        log.info("Buy Response : " + resultBuy);
        if (resultBuy == null) {
            updateStatusFailed(orderBuy);
            updateStatusFailed(orderSell);
            throw new IllegalArgumentException("Failed to access Coinsbit");
        } else {
            orderBuy = updateStatusSuccessCoinsbit(orderBuy, resultBuy);
        }
        CoinsbitOrderDto resultSell = coinsbitService.postOrder(ConstantValue.SIDE_SELL, sellBsiAmount, profitPrice);
        if (resultSell == null) {
            orderSell = updateStatusPending(orderSell);
        } else {
            orderSell = updateStatusSuccessCoinsbit(orderSell, resultSell);
        }
        OrderResponseDto dataBuy = orderBuy.toDtoList();
        OrderResponseDto dataSell = orderSell.toDtoList();
        return new GlobaExchangeMultipleResponse(null, Arrays.asList(dataBuy, dataSell));
    }


    private GlobaExchangeMultipleResponse postMultipleOrderDigifinex(OrderMultipleRequestDto orderRequestDto) {
        MarketSetupEntity marketSetupEntity = marketSetupService.getSetup().get(0);
        DigifinexMarketResponse market = digifinexService.getMarketStatus();
        log.info("Buy Current price : " + market.getTicker().get(0).getLast());

        Double buyUsdAmount = Util.getBuyAmountByRation(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Buy USD Amount : " + buyUsdAmount);

        Double buyBsiAmount = Util.calculatetoBsiAmount(market.getTicker().get(0).getLast(), buyUsdAmount, 4);
        log.info("Buy BSI Amount : " + buyBsiAmount);

        Double profitPrice = Util.calculateProfitPrice(market.getTicker().get(0).getLast(), marketSetupEntity.getProfitPercent(), 4);
        log.info("Sell Profit Price : " + profitPrice);

        Double sellUsdAmount = Util.getSellAmountFromRatio(marketSetupEntity.getBuyPercent(), orderRequestDto.getAmount());
        log.info("Sell USD Amount : " + sellUsdAmount);

        Double sellBsiAmount = Util.calculatetoBsiAmount(profitPrice, sellUsdAmount, 6);
        log.info("Sell BSI Amount : " + sellBsiAmount);

        OrderEntity orderBuy = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_DIGIFINEX, market.getTicker().get(0).getLast(), ConstantValue.SIDE_BUY, buyBsiAmount));
        OrderEntity orderSell = orderRepository.save(orderRequestDto.toOrderEntity(ConstantValue.EXCHANGE_DIGIFINEX, profitPrice, ConstantValue.SIDE_SELL, sellBsiAmount));
        DigifinexStatusResponse resultBuy = digifinexService.postOrder(ConstantValue.SIDE_BUY, buyBsiAmount, market.getTicker().get(0).getLast());
        log.info("Buy Response : " + resultBuy);
        if (resultBuy == null) {
            updateStatusFailed(orderBuy);
            updateStatusFailed(orderSell);
            throw new IllegalArgumentException("Failed to access Digifinex");
        } else {
            orderBuy = updateStatusSuccessDigifinex(orderBuy, resultBuy.getData().get(0));
        }
        DigifinexStatusResponse resultSell = digifinexService.postOrder(ConstantValue.SIDE_SELL, sellBsiAmount, profitPrice);
        if (resultSell == null) {
            orderSell = updateStatusPending(orderSell);
        } else {
            orderSell = updateStatusSuccessDigifinex(orderSell, resultSell.getData().get(0));
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

    private DigifinexStatusDataItem getDetailOrderDigifinex(String orderId) {
        DigifinexEntity digifinex = digifinexRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Detail Order Not Found"));
        try {
            return digifinex.toDto();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
