package com.spring4all.minipay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {

    @GetMapping("/test-wxpay")
    public String testPage() {
        return "testPage/wxpay";
    }

}
