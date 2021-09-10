package com.viaje.market.entity;

import com.viaje.market.base_dto.BaseTimeEntity;
import com.viaje.market.dto.GlobalExchangeResponse;
import com.viaje.market.dto.HotbitErrorDto;
import com.viaje.market.dto.OrderResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "order_data")
public class OrderEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long exchangeOrderIdBuy;

    @Column(unique = true)
    private Long exchangeOrderIdSell;

    private Integer exchangeCode;

    private Integer side;

    private Double amount;

    private Double buy;

    private Double sell;

    private Double currentPrice;

    private Double buyPrice;

    private Double sellPrice;

    private Integer isFee;

    private Integer status;

    private String info;

    private boolean isValid;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", exchangeOrderIdBuy=" + exchangeOrderIdBuy +
                ", exchangeOrderIdSell=" + exchangeOrderIdSell +
                ", exchangeCode=" + exchangeCode +
                ", side=" + side +
                ", amount=" + amount +
                ", buy=" + buy +
                ", sell=" + sell +
                ", currentPrice=" + currentPrice +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", isFee=" + isFee +
                ", status=" + status +
                ", info='" + info + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    public GlobalExchangeResponse toDto(HotbitErrorDto error) {
        return GlobalExchangeResponse.builder()
                .error(error)
                .result(new OrderResponseDto(id, exchangeOrderIdBuy, exchangeOrderIdSell, exchangeCode, side, amount, buy, sell, currentPrice, buyPrice, sellPrice, isFee, status, info, isValid))
                .build();
    }

    public OrderResponseDto toDtoList() {
        return OrderResponseDto.builder()
                .id(id)
                .exchangeOrderIdBuy(exchangeOrderIdBuy)
                .exchangeOrderIdSell(exchangeOrderIdSell)
                .exchangeCode(exchangeCode)
                .side(side)
                .amount(amount)
                .buy(buy)
                .sell(sell)
                .currentPrice(currentPrice)
                .buyPrice(buyPrice)
                .sellPrice(sellPrice)
                .isFee(isFee)
                .status(status)
                .info(info)
                .isValid(isValid)
                .build();
    }
}

