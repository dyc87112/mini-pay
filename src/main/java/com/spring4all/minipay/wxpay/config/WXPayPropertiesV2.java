package com.spring4all.minipay.wxpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 多商户配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay.multi-merchant")
public class WXPayPropertiesV2 {

    private Map<String, WXPayProperties> config = new HashMap<>();

}
