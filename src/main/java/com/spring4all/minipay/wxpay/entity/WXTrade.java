package com.spring4all.minipay.wxpay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "wx_trade")
public class WXTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appid;
    private String mchid;
    private String out_trade_no;
    private String trade_state;
    private String trade_state_desc;
    private String payer_currency;
    private Integer total;

}
