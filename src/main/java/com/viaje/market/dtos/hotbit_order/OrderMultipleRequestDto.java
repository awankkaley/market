package com.viaje.market.dtos.hotbit_order;

import com.viaje.market.entities.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderMultipleRequestDto {

    @NotNull(message = "Please provide a amount")
    private Double amount;

    public OrderEntity toOrderEntity(String exchangeCode, Double price, String side, Double amount) {
        return OrderEntity.builder()
                .exchangeOrderId(null)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .currentPrice(price)
                .side(side)
                .status(0)
                .build();
    }
}
