package com.spring4all.minipay.wxpay.api;

import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.service.WXNativeServiceMgr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Data
@Slf4j
@RestController
@AllArgsConstructor
public class WXPayNotifyController extends WXPayBaseController {

    private WXNativeService wxNativeService;
    private WXNativeServiceMgr wxNativeServiceMgr;

    /**
     * 微信支付的回调处理（单商户）
     *
     * @param body
     * @return
     */
    @PostMapping("/notify/wxpay")
    public String notify(@RequestBody String body,
                         @RequestHeader("Wechatpay-Serial") String serialNumber,
                         @RequestHeader("Wechatpay-Nonce") String nonce,
                         @RequestHeader("Wechatpay-Signature") String signature,
                         @RequestHeader("Wechatpay-Signature-Type") String signatureType,
                         @RequestHeader("Wechatpay-Timestamp") String timestamp) {
        log.info("notify body: {}", body);
        wxNativeService.processNotification(body, serialNumber, nonce,
                signature, signatureType, timestamp);
        return "";
    }

    /**
     * 微信支付的回调处理（多商户）
     *
     * @param body
     * @return
     */
    @PostMapping("/notify/wxpay/{merchantId}")
    public String notifyV2(@PathVariable String merchantId, @RequestBody String body,
                         @RequestHeader("Wechatpay-Serial") String serialNumber,
                         @RequestHeader("Wechatpay-Nonce") String nonce,
                         @RequestHeader("Wechatpay-Signature") String signature,
                         @RequestHeader("Wechatpay-Signature-Type") String signatureType,
                         @RequestHeader("Wechatpay-Timestamp") String timestamp) {
        log.info("notify body: {}", body);
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        wxNativeService.processNotification(body, serialNumber, nonce,
                signature, signatureType, timestamp);
        return "";
    }

}