package com.spring4all.minipay.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> {

    private String code;
    private String message;
    private T detail;

}
