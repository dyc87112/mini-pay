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
     * 查询上个月成功支付的订单总金额
     *
     * @param mchid
     * @param appid
     */
    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid and appid=:appid
              and year(create_time) = year(now())
              and month(create_time) = month(now()) - 1
            """)
    Long findLastMonthSuccessTotalFeeByMchIdAndAppId(String mchid, String appid);

    /**
     * 查询当前月成功支付的订单总金额
     *
     * @param mchid
     * @param appid
     * @return
     */
    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid and appid=:appid
              and year(create_time) = year(now())
              and month(create_time) = month(now())
            """)
    Long findCurrentMonthSuccessTotalFeeByMchIdAndAppId(String mchid, String appid);

    /**
     * 查询上个月成功支付的订单总金额
     *
     * @param mchid
     */
    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid
              and year(create_time) = year(now())
              and month(create_time) = month(now()) - 1
            """)
    Long findLastMonthSuccessTotalFeeByMchId(String mchid);

    /**
     * 查询当前月成功支付的订单总金额
     *
     * @param mchid
     * @return
     */
    @Query(nativeQuery = true, value = """
            select sum(total) from wx_trade 
            where trade_state = 'SUCCESS' and mchid=:mchid
              and year(create_time) = year(now())
              and month(create_time) = month(now())
            """)
    Long findCurrentMonthSuccessTotalFeeByMchId(String mchid);

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
