package com.spring4all.minipay.wxpay.dao;

import com.spring4all.minipay.wxpay.entity.WXTrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface WXTradeQueryRepository
        extends ListPagingAndSortingRepository<WXTrade, Long> {

    Page<WXTrade> findAllByTradeStateIsNot(String tradeState, Pageable pageable);

    Page<WXTrade> findAllByTradeStateEquals(String tradeState, Pageable pageable);

}
