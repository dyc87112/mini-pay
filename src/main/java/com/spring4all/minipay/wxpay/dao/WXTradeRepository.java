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

    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid and appid=:appid
              and year(create_time) = year(CURDATE() - INTERVAL :x MONTH)
              and month(create_time) = month(CURDATE() - INTERVAL :x MONTH)
            """)
    Long findLastXMonthSuccessTotalFeeByMchIdAndAppId(int x, String mchid, String appid);

    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid
              and year(create_time) = year(CURDATE() - INTERVAL :x MONTH)
              and month(create_time) = month(CURDATE() - INTERVAL :x MONTH)
            """)
    Long findLastXMonthSuccessTotalFeeByMchId(int x, String mchid);

    /**
     * 查询超时x分钟的订单
     *
     * @param timeInMinutes
     * @return
     */
    @Query(nativeQuery = true, value =
            "select * from wx_trade where trade_state = 'NOTPAY' " +
                    "and now() > date_add(create_time, interval :timeInMinutes minute)")
    List<WXTrade> findOvertimeTrade(int timeInMinutes);

}
