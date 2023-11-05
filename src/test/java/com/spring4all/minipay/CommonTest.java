package com.spring4all.minipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import kotlin.coroutines.jvm.internal.SuspendFunction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class CommonTest {

    @Test
    void xxxxxx() {
        String a = "2023-08-09T02:34:38+08:00";
        DateTime time = DateUtil.parse(a);
        Date date = new Date(time.getTime());
        System.out.println(date);
    }

    @Test
    void tradeId() {
        int userId = 1; // 用户id
        String now = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        String userIdStr = StrUtil.fillBefore(String.valueOf(userId), '0', 7);
        String random = RandomUtil.randomNumbers(3);
        String tradeId = now + userIdStr + random; // 订单id

        System.out.println(tradeId + ", " + tradeId.length());
    }

}
