package com.spring4all.minipay;

import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MiniPayApplicationTests {

    @Autowired
    private WXNativeService wxNativeService;
    @Autowired
    private WXTradeRepository wxTradeRepository;

    @Test
    void wxTradeRepository() {
        WXTrade trade = new WXTrade();
        trade.setAppid("111");
        wxTradeRepository.save(trade);
    }



    @Test
    void preNativePay() {
        // 商户订单已存在的异常
        // wxNativeService.preNativePay("trade_no_1", "测试商品", 1);
        // 获取二维码
        wxNativeService.preNativePay("trade_no_3", "测试商品", 1);
    }
    @Test
    void queryOrderByOutTradeNo() {
        wxNativeService.queryWXPayTradeByOutTradeNo("trade_no_1");
        wxNativeService.queryWXPayTradeByOutTradeNo("trade_no_2");
    }

    @Test
    void closeOrder() {
        wxNativeService.closeOrderByOutTradeNo("out_trade_no_4");
    }

}
