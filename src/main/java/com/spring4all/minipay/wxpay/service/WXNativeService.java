package com.spring4all.minipay.wxpay.service;

import com.spring4all.minipay.wxpay.WXPayConfig;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
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
        log.info("WX Pay Config init！" +
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
        log.info("WX Pay Service init success");
    }

    public String preNativePay(String tradeNo, String description, int totalFee) {
        PrepayRequest request = new PrepayRequest();
        request.setAppid(wxPayConfig.getAppId());
        request.setMchid(wxPayConfig.getMerchantId());
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(tradeNo);
        request.setDescription(description);

        Amount amount = new Amount();
        amount.setTotal(totalFee);
        request.setAmount(amount);

        PrepayResponse response = nativePayService.prepay(request); // 调用下单方法，得到应答
        return response.getCodeUrl(); // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
    }

}
