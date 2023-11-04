package com.spring4all.minipay.exception;

import lombok.Data;

@Data
public class TradeIsClosedException extends TradeException {

    public TradeIsClosedException() {
        this.errorCode = "500";
        this.errorMessage = "支付订单已经关闭";
    }

}
