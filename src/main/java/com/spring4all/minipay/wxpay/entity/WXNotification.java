package com.spring4all.minipay.wxpay.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity(name = "wx_notification")
@EntityListeners(AuditingEntityListener.class)
public class WXNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String outTradeNo;
    @Column(columnDefinition = "text")
    private String body;
    @Column(columnDefinition = "text")
    private String parse;
    @Column(length = 100)
    private String serialNumber;
    @Column(length = 50)
    private String nonce;
    @Column(length = 500)
    private String signature;
    @Column(length = 100)
    private String signatureType;
    @Column(length = 20)
    private String timestamp;


}
