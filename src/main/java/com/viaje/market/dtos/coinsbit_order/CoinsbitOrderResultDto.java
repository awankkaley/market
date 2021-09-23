package com.viaje.market.dtos.coinsbit_order;

import com.viaje.market.entities.CoinsbitEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CoinsbitOrderResultDto {
    private String side;
    private String amount;
    private String takerFee;
    private long orderId;
    private String dealStock;
    private String dealFee;
    private String type;
    private String market;
    private String makerFee;
    private String left;
    private String price;
    private double timestamp;
    private String dealMoney;

    public CoinsbitEntity toEntity() {
        return CoinsbitEntity.builder()
                .side(side)
                .amount(amount)
                .takerFee(takerFee)
                .orderId(orderId)
                .dealStock(dealStock)
                .dealFee(dealFee)
                .type(type)
                .market(market)
                .makerFee(makerFee)
                .leftData(left)
                .price(price)
                .timestamp(timestamp)
                .dealMoney(dealMoney)
                .build();
    }
}