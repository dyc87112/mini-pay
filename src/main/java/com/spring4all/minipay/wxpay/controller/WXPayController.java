package com.spring4all.minipay.wxpay.controller;

import com.spring4all.minipay.wxpay.service.WXNativeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@Slf4j
@RestController("/wxpay")
@AllArgsConstructor
public class WXPayController {

    private WXNativeService wxNativeService;

    @GetMapping("/pay")
    public String pay(String out_trade_no, String total_fee) throws Exception {
        //调用:统一下单！
        String codeURl = wxNativeService.preNativePay("trade_no_1","测试商品",1);
        return codeURl;
    }

    @GetMapping("/qrcode")
    public void qrcode(@RequestParam("codeUrl") String codeUrl, HttpServletResponse response) throws Exception {
        // TODO 转换支付二维码
        response.setContentType("image/png");

    }

}
