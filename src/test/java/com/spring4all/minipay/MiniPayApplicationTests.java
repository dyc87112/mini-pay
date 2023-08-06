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
        // 获取code url
        String code_url = wxNativeService.preNativePay("trade_no_1", "测试商品", 1);
        // TODO 获取二维码
    }
    @Test
    void queryOrderByOutTradeNo() {
        wxNativeService.queryOrderByOutTradeNo("trade_no_1");
    }

    void closeOrder() {
        wxNativeService.closeOrderByOutTradeNo("trade_no_1");
    }


}
