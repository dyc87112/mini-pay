package com.spring4all.minipay.wxpay.jobs;

import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WXTradeOTJobs {

    private WXNativeService wxNativeService;
    private WXTradeRepository wxTradeRepository;

    @Scheduled(fixedRate = 60000)
    public void overtimeJobs() {
        log.info("WXTradeOTJobs: start!");
        // 查询超过15分钟没有支付的订单，查一下微信订单，如果还未支付就关闭订单
        List<WXTrade> list = wxTradeRepository.findOvertimeTrade(15);
        for(WXTrade wxTrade : list) {
            Transaction t = wxNativeService.queryWXPayTradeByOutTradeNo(wxTrade.getOutTradeNo());
            if(t.getTradeState().equals(Transaction.TradeStateEnum.NOTPAY)) {
                // 本地订单是NOTPAY，微信端也是NOTPAY
                wxNativeService.closeOrderByOutTradeNo(wxTrade.getOutTradeNo());
            } else if(t.getTradeState().equals(Transaction.TradeStateEnum.CLOSED)) {
                // 本地订单是NOTPAY，微信端是CLOSED，仅关闭本地订单
                wxTrade.setTradeState(t.getTradeState().name());
                wxTrade.setTradeStateDesc(t.getTradeStateDesc());
                wxTradeRepository.save(wxTrade);
            } else if(t.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)){
                // 本地订单是NOTPAY，微信端是SUCCESS，更新本地订单

            }

        }
    }

}
