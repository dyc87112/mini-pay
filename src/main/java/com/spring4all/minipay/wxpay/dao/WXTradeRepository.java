package com.spring4all.minipay.wxpay.dao;

import com.spring4all.minipay.wxpay.entity.WXTrade;
import org.springframework.data.repository.CrudRepository;

public interface WXTradeRepository
        extends CrudRepository<WXTrade, Long> {

    WXTrade findByOutTradeNo(String outTradeNo);

}
