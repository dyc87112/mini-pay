package com.spring4all.minipay.wxpay.service;

import com.spring4all.minipay.wxpay.config.WXPayConfig;
import com.spring4all.minipay.wxpay.config.WXPayConfigV2;
import com.spring4all.minipay.wxpay.config.WXPayPropertiesV2;
import com.spring4all.minipay.wxpay.dao.WXNotificationRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付：多商户服务
 */
@Slf4j
@Service
public class WXNativeServiceMgr {

    private Map<String, WXNativeService> wxNativeServiceMap = new HashMap<>();

    public WXNativeServiceMgr(WXPayConfigV2 wxPayConfigV2,
                              WXTradeRepository wXTradeRepository,
                              WXNotificationRepository wxNotificationRepository) {
        for (String key : wxPayConfigV2.getConfig().keySet()) {
            WXPayConfig wxPayConfig = wxPayConfigV2.getConfig().get(key);
            WXNativeService wxNativeService = new WXNativeService(wxPayConfig, wXTradeRepository, wxNotificationRepository);
            wxNativeServiceMap.put(key, wxNativeService);
            log.info("商户：{}，微信支付配置初始化：成功，", key);
        }
    }

    public WXNativeService getWXNativeService(String merchantId) {
        return wxNativeServiceMap.get(merchantId);
    }

}
