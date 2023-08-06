package com.spring4all.minipay.wxpay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WXPayConfig {

    private final static String notifyPath = "/wxpay/notify";

    private String appId;
    private String merchantId;
    private String merchantSerialNumber;
    private String privateKeyPath;
    private String apiV3Key;
    private String notifyUrl;
    private String notifyHost;

    public void setNotifyHost(String notifyHost) {
        this.notifyHost = notifyHost;
        this.notifyUrl = this.notifyHost + notifyPath;
    }
}
