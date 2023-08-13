package com.spring4all.minipay.wxpay.dao;

import com.spring4all.minipay.wxpay.entity.WXTrade;
import org.springframework.data.repository.ListCrudRepository;

public interface WXTradeRepository
        extends ListCrudRepository<WXTrade, Long> {

    WXTrade findByOutTradeNo(String outTradeNo);

}
