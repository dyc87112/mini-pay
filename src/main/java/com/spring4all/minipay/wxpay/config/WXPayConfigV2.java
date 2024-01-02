package com.spring4all.minipay.wxpay.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付：多商户配置
 */
@Slf4j
@Data
@Configuration
public class WXPayConfigV2 {

    private WXPayPropertiesV2 wxPayPropertiesV2;
    private Map<String, WXPayConfig> config = new HashMap<>();

    public WXPayConfigV2(WXPayPropertiesV2 wxPayPropertiesV2) {
        this.wxPayPropertiesV2 = wxPayPropertiesV2;

        for (WXPayProperties wxPayProperties : wxPayPropertiesV2.getConfig().values()) {
            wxPayProperties.setNotifyUrl(wxPayProperties.getNotifyUrl() + "/" + wxPayProperties.getMerchantId());
            WXPayConfig c = new WXPayConfig(wxPayProperties);
            config.put(wxPayProperties.getMerchantId(), c);
        }
    }

}
