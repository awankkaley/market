package com.viaje.market.entity;

import com.viaje.market.base_dto.BaseTimeEntity;
import com.viaje.market.dto.coinsbit_order.CoinsbitOrderResultDto;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "coinsbit_data")
public class CoinsbitEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String side;

    private String amount;

    private String takerFee;

    private Long orderId;

    private String dealStock;

    private String dealFee;

    private String type;

    private String market;

    private String makerFee;

    private String leftData;

    private String price;

    private Double timestamp;

    private String dealMoney;

    public CoinsbitOrderResultDto toDto() {
        return CoinsbitOrderResultDto.builder()
                .side(side)
                .amount(amount)
                .takerFee(takerFee)
                .orderId(orderId)
                .dealStock(dealStock)
                .dealFee(dealFee)
                .type(type)
                .market(market)
                .makerFee(makerFee)
                .left(leftData)
                .price(price)
                .timestamp(timestamp)
                .dealMoney(dealMoney)
                .build();
    }

    @Override
    public String toString() {
        return "CoinsbitEntity{" +
                "id=" + id +
                ", side='" + side + '\'' +
                ", amount='" + amount + '\'' +
                ", takerFee='" + takerFee + '\'' +
                ", orderId=" + orderId +
                ", dealStock='" + dealStock + '\'' +
                ", dealFee='" + dealFee + '\'' +
                ", type='" + type + '\'' +
                ", market='" + market + '\'' +
                ", makerFee='" + makerFee + '\'' +
                ", leftData='" + leftData + '\'' +
                ", price='" + price + '\'' +
                ", timestamp=" + timestamp +
                ", dealMoney='" + dealMoney + '\'' +
                '}';
    }
}
