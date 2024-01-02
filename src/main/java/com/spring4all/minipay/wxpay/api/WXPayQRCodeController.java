package com.spring4all.minipay.wxpay.api;

import com.spring4all.minipay.wxpay.utils.QRCodeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@Slf4j
@RestController
@RequestMapping("/api/wxpay")
public class WXPayQRCodeController extends WXPayBaseController {

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
    public void qrcode(@RequestParam("codeUrl") String codeUrl, HttpServletResponse response) throws Exception {
        log.info("生成支付二维码, codeUrl = {}", codeUrl);
        response.setContentType("image/png");
        QRCodeUtils.writeToStream(codeUrl, response.getOutputStream(), 300, 300);
    }

}