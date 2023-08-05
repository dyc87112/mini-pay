package com.spring4all.minipay.wxpay.utils;

import java.text.SimpleDateFormat;

public class WXUtils {

    /**
     * 所有的日期对象，使用 ISO 8601 所定义的格式。示例：
     *
     * yyyy-MM-DDTHH:mm:ss.SSSZ
     * yyyy-MM-DDTHH:mm:ssZ
     * yyyy-MM-DDTHH:mm:ss.SSS+08:00
     * yyyy-MM-DDTHH:mm:ss+08:00
     */
    public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-DDTHH:mm:ssZ");

}
