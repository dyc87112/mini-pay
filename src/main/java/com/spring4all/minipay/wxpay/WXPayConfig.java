package com.spring4all.minipay.wxpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WXPayConfig {

    private String appId;
    private String merchantId;
    private String merchantSerialNumber;
    private String privateKeyPath;
    private String apiV3Key;
    private String notifyUrl;

}
