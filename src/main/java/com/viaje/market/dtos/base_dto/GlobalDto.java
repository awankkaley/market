package com.viaje.market.dtos.base_dto;

import com.viaje.market.dtos.HotbitErrorDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalDto<T> extends BaseDto {

    private T result;

    @Builder
    public GlobalDto(HotbitErrorDto error, T result) {
        super(error);
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                " error: '" + error + '\'' +
                ", result:" + result +
                '}';
    }
}
