package com.spring4all.minipay.wxpay.controller;

import com.spring4all.minipay.common.CommonResponse;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public class WXPayBaseController {

    @ExceptionHandler(value = ServiceException.class)
    public CommonResponse wxPayServiceException(ServiceException e) {
        return new CommonResponse(e.getErrorCode(), e.getErrorMessage(), e.getResponseBody());
    }

    @ExceptionHandler(value = HttpException.class)
    public CommonResponse wxPayHttpException(HttpException e) {
        log.error("WxPay HttpException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", e.getMessage(), e.getCause());
    }

    @ExceptionHandler(value = ValidationException.class)
    public CommonResponse wxPayValidationException(ValidationException e) {
        log.error("WxPay ValidationException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", e.getMessage(), e.getCause());
    }

    @ExceptionHandler(value = MalformedMessageException.class)
    public CommonResponse wxPayMalformedMessageException(MalformedMessageException e) {
        log.error("WxPay MalformedMessageException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", e.getMessage(), e.getCause());
    }

}
