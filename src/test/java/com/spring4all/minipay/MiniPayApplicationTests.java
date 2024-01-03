package com.spring4all.minipay;

import com.spring4all.minipay.wxpay.dao.WXTradeQueryRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MiniPayApplicationTests {

    @Autowired
    private WXNativeService wxNativeService;
    @Autowired
    private WXTradeRepository wxTradeRepository;
    @Autowired
    private WXTradeQueryRepository wxTradeQueryRepository;

    @Test
    void wxTradeRepository() {
//        Pageable pageable = Pageable.unpaged();
//        wxTradeQueryRepository.findAllByTradeStateIsNot("CLOSED", pageable);
//        Long a = wxTradeRepository.findLastMonthSuccessTotalFee();
//        log.info("{}", a);
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
