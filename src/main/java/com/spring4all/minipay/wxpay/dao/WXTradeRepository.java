package com.spring4all.minipay.wxpay.dao;

import com.spring4all.minipay.wxpay.entity.WXTrade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WXTradeRepository
        extends ListCrudRepository<WXTrade, Long> {

    WXTrade findByOutTradeNo(String outTradeNo);

    WXTrade findByMchidAndAppidAndOutTradeNo(String mchid, String appid, String outTradeNo);

    WXTrade findByMchidAndOutTradeNo(String mchid, String outTradeNo);

    /**
     * 查询超时x分钟的订单
     * @param timeInMinutes
     * @return
     */
    @Query(nativeQuery = true, value =
            "select * from wx_trade where trade_state = 'NOTPAY' " +
                    "and now() > date_add(create_time, interval :timeInMinutes minute)")
    List<WXTrade> findOvertimeTrade(int timeInMinutes);

}
