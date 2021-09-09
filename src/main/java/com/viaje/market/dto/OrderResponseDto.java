package com.viaje.market.dto;

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

    private Double price;

    private Integer isFee;

    private Integer status;

    private String info;

    private boolean isValid;
}
