package com.spring4all.minipay;

import com.spring4all.minipay.wxpay.service.WXNativeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniPayApplicationTests {

    @Autowired
    private WXNativeService wxService;

    @Test
    void contextLoads() {
        wxService.preNativePay(100, "商品标题");
    }

}
