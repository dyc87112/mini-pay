package com.spring4all.minipay;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CommonTest {

    @Test
    void xxxxxx() {
        String a = "2023-08-09T02:34:38+08:00";
        DateTime time = DateUtil.parse(a);
        Date date = new Date(time.getTime());
        System.out.println(date);
    }

}
