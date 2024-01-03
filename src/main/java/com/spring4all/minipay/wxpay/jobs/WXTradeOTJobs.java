package com.spring4all.minipay.wxpay.jobs;

import cn.hutool.core.date.DateUtil;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.service.WXNativeServiceMgr;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WXTradeOTJobs {

    private WXNativeServiceMgr wxNativeServiceMgr;
    private WXTradeRepository wxTradeRepository;

    @Scheduled(fixedRate = 60000)
    public void overtimeJobs() {
        // FIXME 如果只有单商户的话，目前处理不了
        // 查询超过15分钟没有支付的订单，查一下微信订单，如果还未支付就关闭订单
        List<WXTrade> list = wxTradeRepository.findOvertimeTrade(15);
        for(WXTrade wxTrade : list) {
            WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(wxTrade.getMchid());
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
                log.info("订单 {} 状态不一致, 同步信息", wxTrade.getOutTradeNo());
                log.info("WXTrade: {}", wxTrade);
                log.info("Transaction: {}", t);
                wxTrade.setTradeState(t.getTradeState().name());
                wxTrade.setTradeStateDesc(t.getTradeStateDesc());
                wxTrade.setTransactionId(t.getTransactionId());
                wxTrade.setTradeType(t.getTradeType().name());
                wxTrade.setCurrency(t.getAmount().getCurrency());
                wxTrade.setPayerTotal(t.getAmount().getPayerTotal());
                wxTrade.setPayerOpenId(t.getPayer().getOpenid());
                wxTrade.setBankType(t.getBankType());
                wxTrade.setSuccessTime(new Date(DateUtil.parse(t.getSuccessTime()).getTime()));
                wxTradeRepository.save(wxTrade);
            }
        }
    }

}
