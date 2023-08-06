package com.spring4all.minipay.wxpay.service;

import com.spring4all.minipay.wxpay.WXPayConfig;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WXNativeService {

    private WXPayConfig wxPayConfig;
    private Config config;
    private NativePayService nativePayService;

    public WXNativeService(WXPayConfig wxPayConfig) {
        this.wxPayConfig = wxPayConfig;
        log.info("微信支付配置初始化：" +
                        "\n---MerchantId={} " +
                        "\n---MerchantSerialNumber={} " +
                        "\n---PrivateKeyPath={}, ",
                wxPayConfig.getMerchantId(), wxPayConfig.getMerchantSerialNumber(), wxPayConfig.getPrivateKeyPath());

        this.config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayConfig.getMerchantId())
                .privateKeyFromPath(wxPayConfig.getPrivateKeyPath())
                .merchantSerialNumber(wxPayConfig.getMerchantSerialNumber())
                .apiV3Key(wxPayConfig.getApiV3Key())
                .build();
        this.nativePayService = new NativePayService.Builder().config(config).build();
        log.info("微信支付配置初始化：成功！");
    }

    public String preNativePay(String outTradeNo, String description, int totalFee) {
        PrepayRequest request = new PrepayRequest();
        request.setAppid(wxPayConfig.getAppId());
        request.setMchid(wxPayConfig.getMerchantId());
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        request.setDescription(description);

        Amount amount = new Amount();
        amount.setTotal(totalFee);
        request.setAmount(amount);

        PrepayResponse response = nativePayService.prepay(request); // 调用下单方法，得到应答
        return response.getCodeUrl(); // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
    }

    /**
     * 订单查询
     *
     * @param outTradeNo
     * @return
     */
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        queryRequest.setMchid(wxPayConfig.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);

        try {
            Transaction result = nativePayService.queryOrderByOutTradeNo(queryRequest);
            log.info("订单查询：{}", result);
            return result;
        } catch (ServiceException e) {
            // API返回失败, 例如ORDER_NOT_EXISTS
            log.error("code={}, message={}", e.getErrorCode(), e.getErrorMessage());
            log.error("reponse body={}", e.getResponseBody());
            throw e;
        }
    }

    /**
     * 关闭订单
     * @param outTradeNo
     */
    public void closeOrderByOutTradeNo(String outTradeNo) {
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(wxPayConfig.getMerchantId());
        closeRequest.setOutTradeNo(outTradeNo);

        // 方法没有返回值，意味着成功时API返回204 No Content
        nativePayService.closeOrder(closeRequest);
    }

}
