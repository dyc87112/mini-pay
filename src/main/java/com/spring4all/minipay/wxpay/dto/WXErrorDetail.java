package com.spring4all.minipay.wxpay.dto;

import lombok.Data;

@Data
public class WXErrorDetail {

    private String filed;
    private String value;
    private String issue;
    private String location;

}
