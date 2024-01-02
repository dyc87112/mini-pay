package com.spring4all.minipay.wxpay.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付：单商户配置
 */
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
