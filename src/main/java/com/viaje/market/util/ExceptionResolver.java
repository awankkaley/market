package com.viaje.market.util;

import com.viaje.market.base_dto.BaseError;
import com.viaje.market.base_dto.GlobalErrorDto;
import com.viaje.market.base_dto.GlobalMultipleErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@RestControllerAdvice
public class ExceptionResolver {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<GlobalMultipleErrorDto> constraintHandler(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        GlobalMultipleErrorDto GlobalErrorResponse = new GlobalMultipleErrorDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<GlobalErrorDto> unauthenticatedHandler(AuthenticationException ex) {
        if (ex.getMessage().equals("No value present"))
            ex = new InternalAuthenticationServiceException("username or password is wrong");
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(new BaseError(9101, "Invalid Authentication"));
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<GlobalErrorDto> globalHandler(IllegalArgumentException ex) {
        GlobalErrorDto globalErrorResponse = new GlobalErrorDto(new BaseError(9102, ex.getMessage()));
        return new ResponseEntity<>(globalErrorResponse, HttpStatus.OK);

    }
}

