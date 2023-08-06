package com.spring4all.minipay.wxpay;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
public class WXPayConfig {

    private WXPayProperties wxPayProperties;

    private RSAAutoCertificateConfig config;

    public WXPayConfig(WXPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;

        log.info("微信支付配置初始化：" +
                        "\n---MerchantId={} " +
                        "\n---MerchantSerialNumber={} " +
                        "\n---PrivateKeyPath={}, " +
                        "\n---NotifyUrl={}",
                wxPayProperties.getMerchantId(), wxPayProperties.getMerchantSerialNumber(),
                wxPayProperties.getPrivateKeyPath(), wxPayProperties.getNotifyUrl());

        this.config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMerchantId())
                .privateKeyFromPath(wxPayProperties.getPrivateKeyPath())
                .merchantSerialNumber(wxPayProperties.getMerchantSerialNumber())
                .apiV3Key(wxPayProperties.getApiV3Key())
                .build();
    }

}
