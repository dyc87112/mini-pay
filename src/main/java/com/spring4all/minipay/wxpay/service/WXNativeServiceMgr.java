package com.spring4all.minipay.wxpay.service;

import com.spring4all.minipay.wxpay.config.WXPayConfig;
import com.spring4all.minipay.wxpay.config.WXPayConfigV2;
import com.spring4all.minipay.wxpay.config.WXPayProperties;
import com.spring4all.minipay.wxpay.config.WXPayPropertiesV2;
import com.spring4all.minipay.wxpay.dao.WXNotificationRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 微信支付：多商户服务
 */
@Slf4j
@Data
@Service
public class WXNativeServiceMgr {

    private Map<String, WXNativeService> wxNativeServiceMap = new HashMap<>();
    private WXPayConfigV2 wxPayConfigV2;

    public WXNativeServiceMgr(WXPayConfigV2 wxPayConfigV2,
                              WXTradeRepository wXTradeRepository,
                              WXNotificationRepository wxNotificationRepository) {
        this.wxPayConfigV2 = wxPayConfigV2;
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

    /**
     * 获取配置的商户ID集合
     * @return
     */
    public Set<String> merchantSet() {
        return wxPayConfigV2.getConfig().keySet();
    }

    /**
     * 获取商户下配置的appid
     * @param merchantId
     * @return
     */
    public List<String> appIds(String merchantId) {
        return Arrays.stream(wxPayConfigV2.getConfig().get(merchantId).getWxPayProperties().getAppId().split(",")).toList();
    }

    public WXPayProperties getWXPayProperties(String merchantId) {
        return wxPayConfigV2.getConfig().get(merchantId).getWxPayProperties();
    }

}