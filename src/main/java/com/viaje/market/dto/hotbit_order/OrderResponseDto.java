package com.viaje.market.dto.hotbit_order;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {

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
}
