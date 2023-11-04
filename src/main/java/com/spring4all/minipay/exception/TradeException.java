package com.spring4all.minipay.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TradeException extends RuntimeException {

    public String errorCode;
    public String errorMessage;

}
