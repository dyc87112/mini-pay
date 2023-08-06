package com.spring4all.minipay;

import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MiniPayApplicationTests {

    @Autowired
    private WXNativeService wxNativeService;
    @Autowired
    private WXTradeRepository wxTradeRepository;

    @Test
    void wxtrade() {
        WXTrade trade = new WXTrade();
        trade.setAppid("111");
        wxTradeRepository.save(trade);


    }

    @Test
    void preNativePay() {
        // 商户订单已存在的异常
//        wxNativeService.preNativePay("trade_no_1", "测试商品", 1);
        // TODO 获取二维码
        wxNativeService.preNativePay("trade_no_3", "测试商品", 1);
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
