package com.spring4all.minipay.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import static com.spring4all.minipay.admin.controller.PageLayout.page_layout_1;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        model.addAttribute("content", "dashboard");
        return page_layout_1;
    }

}
