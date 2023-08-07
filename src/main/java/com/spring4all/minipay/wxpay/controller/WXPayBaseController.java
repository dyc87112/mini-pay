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
        // 当发生 HTTP 请求异常时抛出该异常。构建请求参数失败、发送请求失败、I/O错误：推荐上报监控和打印日志，并获取异常中的 HTTP 请求信息以定位问题。
        log.error("WxPay HttpException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", "调用微信支付服务", "");
    }

    @ExceptionHandler(value = ValidationException.class)
    public CommonResponse wxPayValidationException(ValidationException e) {
        // 可能问题：
        // 1. 验证微信支付返回签名失败：上报监控和日志打印。
        // 2. 验证微信支付回调通知签名失败：确认输入参数与 HTTP 请求信息是否一致，若一致，说明该回调通知参数被篡改导致验签失败。
        log.error("WxPay ValidationException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", "验证微信支付签名失败", "");
    }

    @ExceptionHandler(value = MalformedMessageException.class)
    public CommonResponse wxPayMalformedMessageException(MalformedMessageException e) {
        // 可能问题：
        // 1. HTTP 返回 Content-Type 不为 application/json：不支持其他类型的返回体，下载账单 应使用 download() 方法。
        // 2. 解析 HTTP 返回体失败：上报监控和日志打印。
        // 3. 回调通知参数不正确：确认传入参数是否与 HTTP 请求信息一致，传入参数是否存在编码或者 HTML 转码问题。
        // 4. 解析回调请求体为 JSON 字符串失败：上报监控和日志打印。
        // 5. 解密回调通知内容失败：确认传入的 apiV3 密钥是否正确。
        log.error("WxPay MalformedMessageException : {}, {}", e.getMessage(), e);
        return new CommonResponse("500", "服务返回成功，返回内容异常", "");
    }

}
