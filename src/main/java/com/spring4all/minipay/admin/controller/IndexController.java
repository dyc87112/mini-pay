package com.spring4all.minipay.admin.controller;

import com.spring4all.minipay.wxpay.dao.WXTradeQueryRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.service.WXNativeServiceMgr;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

import static com.spring4all.minipay.admin.controller.PageLayout.page_layout_1;

@Controller
@Slf4j
@AllArgsConstructor
public class IndexController {

    private WXTradeRepository wxTradeRepository;
    private WXNativeService wxNativeService;
    private WXNativeServiceMgr wxNativeServiceMgr;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        // 页面内容
        model.addAttribute("content", "dashboard");
        model.addAttribute("page_title", "数据汇总");
        model.addAttribute("page_pretitle", "控制台");

        // 单商户汇总数据
        String mchid =  wxNativeService.getWxPayProperties().getMerchantId();
        Long lastMonthFee = wxTradeRepository.findLastXMonthSuccessTotalFeeByMchId(1, mchid);
        Long currentMonthFee = wxTradeRepository.findLastXMonthSuccessTotalFeeByMchId(0, mchid);
        model.addAttribute("mchid", mchid);
        model.addAttribute("lastMonthFee", lastMonthFee == null ? 0 : lastMonthFee / 100.0);
        model.addAttribute("currentMonthFee", currentMonthFee == null ? 0 : currentMonthFee / 100.0);

        // 多商户汇总数据
        List<Map<String, Object>> multiMchList  = new ArrayList<>();
        for(String mid : wxNativeServiceMgr.merchantSet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("mchid", mid);
            Long _lastMonthFee = wxTradeRepository.findLastXMonthSuccessTotalFeeByMchId(1, mid);
            Long _currentMonthFee = wxTradeRepository.findLastXMonthSuccessTotalFeeByMchId(0, mid);
            map.put("lastMonthFee", _lastMonthFee == null ? 0 : _lastMonthFee / 100.0);
            map.put("currentMonthFee", _currentMonthFee == null ? 0 : _currentMonthFee / 100.0);
            multiMchList.add(map);
        }
        model.addAttribute("multiMchList", multiMchList);
        return page_layout_1;
    }

}
