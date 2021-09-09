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
    private Long exchangeOrderId;

    private Integer exchangeCode;

    private Integer side;

    private Double amount;

    private Double price;

    private Integer isFee;

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
                ", price=" + price +
                ", isFee=" + isFee +
                ", status=" + status +
                ", info='" + info + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    public GlobalExchangeResponse toDto(HotbitErrorDto error) {
        return GlobalExchangeResponse.builder()
                .error(error)
                .result(new OrderResponseDto(id, exchangeOrderId, exchangeCode, side, amount, price, isFee, status, info, isValid))
                .build();
    }

    public OrderResponseDto toDtoList() {
        return OrderResponseDto.builder()
                .id(id)
                .exchangeOrderId(exchangeOrderId)
                .exchangeCode(exchangeCode)
                .side(side)
                .amount(amount)
                .price(price)
                .isFee(isFee)
                .status(status)
                .info(info)
                .isValid(isValid)
                .build();
    }
}

