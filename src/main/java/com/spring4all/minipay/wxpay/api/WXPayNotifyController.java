package com.spring4all.minipay.wxpay.api;

import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Data
@Slf4j
@RestController
@AllArgsConstructor
public class WXPayNotifyController extends WXPayBaseController {

    private final WXTradeRepository wXTradeRepository;

    private WXNativeService wxNativeService;

    /**
     * 微信支付的回调处理
     *
     * @param body
     * @return
     */
    @PostMapping("/wxpay/notify")
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

}