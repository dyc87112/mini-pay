package com.spring4all.minipay.wxpay.controller;

import com.spring4all.minipay.common.CommonResponse;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.utils.QRCodeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Data
@Slf4j
@RestController
@RequestMapping("/wxpay")
@AllArgsConstructor
public class WXPayController {

    private WXNativeService wxNativeService;

    /**
     * 下单接口，获取微信支付的链接
     *
     * @param tradeNo，用户端生成
     * @param skuName
     * @param totalFee，单位:分
     * @return
     * @throws Exception
     */
    @GetMapping("/prepay")
    public CommonResponse<String> prepay(@RequestParam String tradeNo,
                                         @RequestParam String skuName,
                                         @RequestParam int totalFee) throws Exception {
        log.info("预支付：tradeNo = {}, skuName = {}, totalFee = {}", tradeNo, skuName, totalFee);
        String codeURl = wxNativeService.preNativePay(tradeNo, skuName, totalFee);
        log.info("预支付：tradeNo = {}, codeURl = {}", tradeNo, codeURl);
        return new CommonResponse<String>("200", "预支付", codeURl);
    }

    /**
     * 获取支付二维码图片
     * 请求例子：wxpay/qrcode?codeUrl=weixin://wxpay/bizpayurl?pr=eJuVi9pzz
     * 其中，codeUrl通过调用 /wxpay/prepay 接口获取
     *
     * @param codeUrl
     * @param response
     * @throws Exception
     */
    @GetMapping("/qrcode")
    public void qrcode(@RequestParam("codeUrl") String codeUrl,
                       HttpServletResponse response) throws Exception {
        log.info("生成支付二维码, codeUrl = {}", codeUrl);
        response.setContentType("image/png");
        QRCodeUtils.writeToStream(codeUrl, response.getOutputStream(), 300, 300);
    }

    /**
     * @param body
     * @return
     */
    @PostMapping("/notify")
    public String notify(@RequestBody String body,
                         @RequestHeader("Wechatpay-Serial") String serialNumber,
                         @RequestHeader("Wechatpay-Nonce") String nonce,
                         @RequestHeader("Wechatpay-Signature") String signature,
                         @RequestHeader("Wechatpay-Signature-Type") String signatureType,
                         @RequestHeader("Wechatpay-Timestamp") String timestamp) {
        log.info("notify body: {}", body);
        wxNativeService.processNotification(body, serialNumber, nonce, signature, signatureType, timestamp);
        return "";
    }

}
