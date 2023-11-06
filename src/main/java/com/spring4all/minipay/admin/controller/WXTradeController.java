package com.spring4all.minipay.admin.controller;

import com.spring4all.minipay.wxpay.dao.WXTradeQueryRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.spring4all.minipay.admin.controller.PageLayout.page_layout_1;

@AllArgsConstructor
@Controller
@Slf4j
@RequestMapping("/wxpay")
public class WXTradeController {

    private WXTradeRepository wxTradeRepository;
    private WXTradeQueryRepository wxTradeQueryRepository;
    private WXNativeService wxNativeService;

    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("page_title", "流程测试");
        model.addAttribute("page_pretitle", "微信支付");
        model.addAttribute("content", "wxpay/test");
        return page_layout_1;
    }

    @GetMapping("/list")
    public String tradeList(@PageableDefault(size = 15, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false) String tradeState, Model model) {
        if (tradeState == null || tradeState.isEmpty()) {
            model.addAttribute("page", wxTradeQueryRepository.findAllByTradeStateIsNot("CLOSED", pageable));
        } else {
            model.addAttribute("page", wxTradeQueryRepository.findAllByTradeStateEquals(tradeState, pageable));
        }
        model.addAttribute("tradeState", tradeState);
        model.addAttribute("page_title", "订单列表");
        model.addAttribute("page_pretitle", "微信支付");
        model.addAttribute("content", "wxpay/list");
        return page_layout_1;
    }

    @GetMapping("/detail")
    public String tradeDetail(@RequestParam("outTradeNo") String outTradeNo, Model model) {
        model.addAttribute("page_title", "订单详情");
        model.addAttribute("page_pretitle", "微信支付");
        model.addAttribute("content", "wxpay/detail");
        model.addAttribute("wxTrade", wxNativeService.queryLocalTradeByOutTradeNo(outTradeNo));
        model.addAttribute("wxTransaction", wxNativeService.queryWXPayTradeByOutTradeNo(outTradeNo));
        return page_layout_1;
    }

}