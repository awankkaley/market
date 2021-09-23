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
    @NotNull(message = "Please provide a side")
    @Min(value = 1, message = "Please input a correct format")
    @Max(value = 2, message = "Please input a correct format")
    private Integer side;

    @NotNull(message = "Please provide a amount")
    private Double amount;


    public OrderEntity toOrderEntity(Integer exchangeCode, Double price) {
        return OrderEntity.builder()
                .exchangeCode(exchangeCode)
                .amount(amount)
                .currentPrice(price)
                .side(side)
                .status(0)
                .build();
    }

}
