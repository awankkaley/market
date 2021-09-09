package com.viaje.market.dto;

import com.viaje.market.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    @NotBlank(message = "Please provide a side")
    @Size(min = 1, max = 1, message = "Length is 1")
    private Integer side;

    @NotBlank(message = "Please provide a amount")
    private Double amount;

    @NotBlank(message = "Please provide a price")
    private Double price;

    @NotBlank(message = "Please provide a isFee")
    private Integer isfee;

    public OrderEntity toOrderEntity(Integer exchangeCode, Long exchangeOrderId, Integer status) {
        return OrderEntity.builder()
                .exchangeOrderId(exchangeOrderId)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .side(side)
                .price(price)
                .isFee(isfee)
                .status(status)
                .build();
    }

    public OrderEntity toOrderEntity(Integer exchangeCode) {
        return OrderEntity.builder()
                .exchangeOrderId(null)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .side(side)
                .price(price)
                .isFee(isfee)
                .status(0)
                .build();
    }

    public OrderEntity toOrderEntity(Integer exchangeCode, Integer status) {
        return OrderEntity.builder()
                .exchangeOrderId(null)
                .exchangeCode(exchangeCode)
                .amount(amount)
                .side(side)
                .price(price)
                .isFee(isfee)
                .status(status)
                .build();
    }
}
