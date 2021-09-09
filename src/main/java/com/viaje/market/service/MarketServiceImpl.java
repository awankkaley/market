package com.viaje.market.service;

import com.viaje.market.entity.OrderEntity;
import com.viaje.market.repository.OrderRepository;
import com.viaje.market.api_key.ApiKeyConfiguration;
import com.viaje.market.dto.*;
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
        log.error(payload);
        signatureService.isValidSignature(payload, signature);
        if (exchange == 1) {
            return hotbitService.getListOfTransaction(Integer.valueOf(side), Integer.valueOf(offset), limit);
        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public GlobalExchangeResponse postOrder(Integer exchange, OrderRequestDto orderRequestDto, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&side=" + orderRequestDto.getSide() + "&amount=" + orderRequestDto.getAmount().toString() + "&price=" + orderRequestDto.getPrice().toString() + "&isfee=" + orderRequestDto.getIsfee();
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.save(orderRequestDto.toOrderEntity(exchange));
        if (exchange == 1) {
            HotbitOrderResponseDto result = hotbitService.postOrder(orderRequestDto.getSide(), orderRequestDto.getAmount(), orderRequestDto.getPrice(), orderRequestDto.getIsfee());
            if (result.getError() == null) {
                order.setExchangeOrderId(result.getResult().getId());
                order.setStatus(1);
                order.setValid(true);
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
    public GlobalExchangeResponse cancelOrder(Integer exchange, Long orderId, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&exchange=" + exchange + "&orderId=" + orderId;
        signatureService.isValidSignature(payload, signature);
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("Data Not Found")
        );

        if (exchange == 1) {
            HotbitOrderResponseDto result = hotbitService.cancelOrder(order.getExchangeOrderId());
            if (result.getError() == null) {
                order.setStatus(2);
                order.setInfo("Cancel by Viaje");
                order.setValid(true);
            } else {
                order.setInfo(result.getError().getMessage());
            }
            OrderEntity result2 = orderRepository.save(order);
            return result2.toDto(result.getError());

        } else {
            throw new IllegalArgumentException("Exchange Not Found");
        }
    }

    @Override
    public List<OrderResponseDto> getAllGlobalOrder(Integer page, Integer limit, String signature) {
        String payload = "x-api-key=" + apiKeyConfiguration.getPrincipalRequestValue() + "&page=" + page + "&limit=" + limit;
        signatureService.isValidSignature(payload, signature);
        Pageable paging = PageRequest.of(page, limit, Sort.by("id").descending());
        return orderRepository.findAllOrdersWithPagination(paging).stream()
                .map(OrderEntity::toDtoList)
                .collect(Collectors.toList());
    }
}
