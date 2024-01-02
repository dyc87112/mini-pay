package com.spring4all.minipay.wxpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 单商户配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WXPayProperties {

    private String appId;
    private String merchantId;
    private String merchantSerialNumber;
    private String privateKeyPath;
    private String apiV3Key;
    private String notifyUrl;
    private String notifyHost;
    private String notifyPath = "/notify/wxpay";

    public void setNotifyHost(String notifyHost) {
        this.notifyHost = notifyHost;

        // 设置回调URL
        boolean a = this.notifyHost.endsWith("/");
        boolean b = this.notifyPath.startsWith("/");
        if(a && b) {
            this.notifyUrl = this.notifyHost + this.notifyPath.substring(1);
        } else if(a || b) {
            this.notifyUrl = this.notifyHost + this.notifyPath;
        } else {
            this.notifyUrl = this.notifyHost + "/" + this.notifyPath;
        }
    }

}
