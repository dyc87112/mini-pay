package com.spring4all.minipay.wxpay.controller;

import com.spring4all.minipay.common.CommonResponse;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.utils.QRCodeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param tradeNo
     * @param description
     * @param totalFee
     * @return
     * @throws Exception
     */
    @GetMapping("/prepay")
    public CommonResponse<String> prepay(@RequestParam String tradeNo,
                                         @RequestParam String skuName,
                                         @RequestParam int totalFee) throws Exception {
        log.info("下单准备：tradeNo = {}, skuName = {}, totalFee = {}", tradeNo, skuName, totalFee);
        String codeURl = wxNativeService.preNativePay(tradeNo, skuName, totalFee);
        log.info("下单准备：tradeNo = {}, codeURl = {}", codeURl);
        return new CommonResponse<String>("200", "准备下单", codeURl);
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

}
