package com.viaje.market.dtos.hotbit_order;

import com.viaje.market.entities.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    @NotBlank(message = "Please provide a side (buy or sell)")
    private String side;

    @NotNull(message = "Please provide a amount")
    private Double amount;

    private Double price;

    public OrderEntity toOrderEntity(String exchangeCode, Double price) {
        return OrderEntity.builder()
                .exchangeCode(exchangeCode)
                .amount(amount)
                .currentPrice(price)
                .side(side)
                .status(0)
                .build();
    }

}
