package com.viaje.market.base_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalDto<T> extends BaseDto {

    private T result;

    @Builder
    public GlobalDto(Integer code, String message, T result) {
        super(code, message);
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                "code: " + code +
                ", message: '" + message + '\'' +
                ", result:" + result +
                '}';
    }
}
