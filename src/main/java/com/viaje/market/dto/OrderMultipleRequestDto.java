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


    public OrderEntity toOrderEntity(Integer exchangeCode, Double price) {
        return OrderEntity.builder()
                .exchangeOrderIdBuy(null)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .currentPrice(price)
                .sell(Util.getAmountSellFromPercentage(buyPercent, amount))
                .buy(Util.getAmountBuyFromPercentage(buyPercent, amount))
                .side(3)
                .isFee(isfee)
                .status(0)
                .build();
    }
}
