package com.viaje.market.entities;

import com.viaje.market.dtos.base_dto.BaseTimeEntity;
import com.viaje.market.dtos.GlobalExchangeResponse;
import com.viaje.market.dtos.HotbitErrorDto;
import com.viaje.market.dtos.hotbit_order.OrderResponseDto;
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

    private Long exchangeOrderId;

    private Integer exchangeCode;

    private Integer side;

    private Double amount;

    private Double currentPrice;

    private Double price;

    private Integer status;

    private String info;

    private boolean isValid;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", exchangeOrderId=" + exchangeOrderId +
                ", exchangeCode=" + exchangeCode +
                ", side=" + side +
                ", amount=" + amount +
                ", currentPrice=" + currentPrice +
                ", price=" + price +
                ", status=" + status +
                ", info='" + info + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    public GlobalExchangeResponse toDto(HotbitErrorDto error) {
        return GlobalExchangeResponse.builder()
                .error(error)
                .result(new OrderResponseDto(id, exchangeOrderId, exchangeCode, side, amount, currentPrice, price, status, info, isValid))
                .build();
    }

    public OrderResponseDto toDtoList() {
        return OrderResponseDto.builder()
                .id(id)
                .exchangeOrderId(exchangeOrderId)
                .exchangeCode(exchangeCode)
                .side(side)
                .amount(amount)
                .currentPrice(currentPrice)
                .price(price)
                .status(status)
                .info(info)
                .isValid(isValid)
                .build();
    }
}

