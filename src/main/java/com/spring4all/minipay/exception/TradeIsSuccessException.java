package com.spring4all.minipay.exception;

import lombok.Data;

@Data
public class TradeIsSuccessException extends TradeException {

    public TradeIsSuccessException() {
        this.errorCode = "500";
        this.errorMessage = "该订单已经完成";
    }
}
