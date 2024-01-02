package com.spring4all.minipay.wxpay.entity;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity(name = "wx_trade")
@EntityListeners(AuditingEntityListener.class)
public class WXTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appid;
    private String mchid;
    @Column(unique = true)
    private String outTradeNo;
    private String outTradeTitle;
    private String tradeState;
    private String tradeStateDesc;
    private String tradeType;
    private String codeUrl;
    private String currency;
    private Integer total;
    private String payerCurrency;
    private Integer payerTotal;
    private String payerOpenId;
    private String bankType;
    private String transactionId;
    private Date successTime;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date modifyTime;

    public void prepayUpdate(Transaction t) {
        this.setAppid(t.getAppid());
        this.setMchid(t.getMchid());

        this.setOutTradeNo(t.getOutTradeNo());

        this.setTotal(t.getAmount().getTotal());
        this.setPayerCurrency(t.getAmount().getPayerCurrency());

        this.setTradeState(t.getTradeState().name());
        this.setTradeStateDesc(t.getTradeStateDesc());
    }

    public void notificationUpdate(Transaction t) {
        // {"amount":{"currency":"CNY","payer_currency":"CNY","payer_total":1,"total":1},
        // "appid":"wxe50aa6193fc2252e","attach":"","bank_type":"OTHERS","mchid":"1596580851",
        // "out_trade_no":"trade_no_2","payer":{"openid":"oJIIz6ozMiqxG2-r166S4cIbY4J8"},
        // "success_time":"2023-08-06T17:06:14+08:00","trade_state":"SUCCESS",
        // "trade_state_desc":"支付成功","trade_type":"NATIVE","transaction_id":"4200001900202308063951583810"}

//        DateTime time = DateUtil.parse(t.getSuccessTime());
//        Date date = new Date(time.getTime());

        this.setTransactionId(t.getTransactionId());
        this.setSuccessTime(DateUtil.parse(t.getSuccessTime()));

        this.setTradeState(t.getTradeState().name());
        this.setTradeStateDesc(t.getTradeStateDesc());
        this.setTradeType(t.getTradeType().name());

        this.setPayerOpenId(t.getPayer().getOpenid());
        this.setBankType(t.getBankType());

        this.setCurrency(t.getAmount().getCurrency());
        this.setPayerTotal(t.getAmount().getPayerTotal());
    }

}
