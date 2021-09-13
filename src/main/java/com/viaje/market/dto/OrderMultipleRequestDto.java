package com.viaje.market.dto;

import com.viaje.market.entity.OrderEntity;
import com.viaje.market.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderMultipleRequestDto {

    @NotBlank(message = "Please provide a amount")
    private Double amount;

    @NotBlank(message = "Please provide a buy ratio")
    private Integer buyPercent;

    @NotBlank(message = "Please provide a isFee")
    private Integer isfee;


    public OrderEntity toOrderEntity(Integer exchangeCode, Double price, Integer side, Double amount) {
        return OrderEntity.builder()
                .exchangeOrderId(null)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .currentPrice(price)
                .side(side)
                .isFee(isfee)
                .status(0)
                .build();
    }
}
