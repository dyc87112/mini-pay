package com.spring4all.minipay.exception;

import lombok.Data;

@Data
public class TradeIsClosedException extends RuntimeException {

    private String errorCode = "500";
    private String errorMessage = "支付订单已经关闭";

}
