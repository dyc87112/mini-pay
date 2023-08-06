package com.spring4all.minipay;

import com.spring4all.minipay.wxpay.service.WXNativeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MiniPayApplicationTests {

    @Autowired
    private WXNativeService wxNativeService;

    @Test
    void preNativePay() {
        // 商户订单已存在的异常
//        wxNativeService.preNativePay("trade_no_1", "测试商品", 1);
        // TODO 获取二维码
        wxNativeService.preNativePay("trade_no_2", "测试商品", 1);
    }
    @Test
    void queryOrderByOutTradeNo() {
        wxNativeService.queryOrderByOutTradeNo("trade_no_1");
        wxNativeService.queryOrderByOutTradeNo("trade_no_2");
    }

    @Test
    void closeOrder() {
        wxNativeService.closeOrderByOutTradeNo("trade_no_1");
    }


}
