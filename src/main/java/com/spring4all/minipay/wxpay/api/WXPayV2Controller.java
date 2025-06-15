package com.spring4all.minipay.wxpay.api;

import cn.hutool.core.date.DateUtil;
import com.spring4all.minipay.common.CommonResponse;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.service.WXNativeServiceMgr;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Data
@Slf4j
@RestController
@RequestMapping("/api/wxpay")
@AllArgsConstructor
public class WXPayV2Controller extends WXPayBaseController {

    private WXTradeRepository wxTradeRepository;
    private WXNativeServiceMgr wxNativeServiceMgr;

    /**
     * 下单接口，获取微信支付的链接
     *
     * @param merchantId
     * @param appId
     * @param outTradeNo，商户订单号（用户端自己生成）
     * @param description，订单描述
     * @param totalFee，单位：分
     * @return
     * @throws Exception
     */
    @GetMapping("/prepay/{merchantId}")
    public CommonResponse<String> prepayV2(@PathVariable String merchantId,
                                           @RequestParam String appId, @RequestParam String outTradeNo,
                                           @RequestParam String description, @RequestParam int totalFee) {
        log.info("预支付：outTradeNo = {}, skuName = {}, totalFee = {}", outTradeNo, description, totalFee);
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        String codeURl = wxNativeService.preNativePay(appId, outTradeNo, description, totalFee);
        log.info("预支付：outTradeNo = {}, codeURl = {}", outTradeNo, codeURl);
        return new CommonResponse<String>("200", "预支付", codeURl);
    }

    /**
     * POST版本的下单接口，获取微信支付的链接
     * 使用POST请求体传递参数，解决订单描述可能出现乱码的问题
     *
     * @param merchantId 商户ID
     * @param request 预支付请求体
     * @return 预支付结果，包含支付二维码链接
     */
    @PostMapping("/prepay/{merchantId}")
    public CommonResponse<String> prepayV2Post(@PathVariable String merchantId,
                                               @RequestBody PrepayRequest request) {
        log.info("POST预支付：outTradeNo = {}, description = {}, totalFee = {}", 
                request.getOutTradeNo(), request.getDescription(), request.getTotalFee());
        
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        String codeURl = wxNativeService.preNativePay(request.getAppId(), request.getOutTradeNo(), 
                                                     request.getDescription(), request.getTotalFee());
        log.info("POST预支付：outTradeNo = {}, codeURl = {}", request.getOutTradeNo(), codeURl);
        return new CommonResponse<String>("200", "预支付成功", codeURl);
    }

    /**
     * 查询微信支付订单状态，并更新本地状态
     *
     * @param outTradeNo
     * @return
     */
    @GetMapping("/queryWXPayTrade/{merchantId}")
    public CommonResponse<Transaction> queryWXPayTrade(@PathVariable String merchantId,
                                                       @RequestParam String outTradeNo) {
        log.info("查询微信支付订单 outTradeNo: {}", outTradeNo);
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        Transaction t = wxNativeService.queryWXPayTradeByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "查询成功：" + outTradeNo, t);
    }

    @GetMapping("/queryLocalTrade/{merchantId}")
    public CommonResponse<WXTrade> queryLocalTrade(@PathVariable String merchantId,
                                                   @RequestParam String outTradeNo) {
        log.info("查询本地支付订单 outTradeNo: {}", outTradeNo);
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        WXTrade t = wxNativeService.queryLocalTradeByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "查询本地订单成功", t);
    }

    @GetMapping("/queryTrade/{merchantId}")
    public CommonResponse<WXTrade> queryTrade(@PathVariable String merchantId,
                                              @RequestParam String outTradeNo) {
        log.info("查询本地订单和微信订单，返回最新状态: {}", outTradeNo);
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        WXTrade wxTrade = wxNativeService.queryLocalTradeByOutTradeNo(outTradeNo);
        Transaction transaction = wxNativeService.queryWXPayTradeByOutTradeNo(outTradeNo);
        if (!wxTrade.getTradeState().equals(transaction.getTradeState().name())) {
            // 状态不同，拿transaction 更新
            log.info("订单 {} 状态不一致, 同步信息", outTradeNo);
            log.info("WXTrade: {}", wxTrade);
            log.info("Transaction: {}", transaction);

            wxTrade.setTradeState(transaction.getTradeState().name());
            wxTrade.setTradeStateDesc(transaction.getTradeStateDesc());
            wxTrade.setTransactionId(transaction.getTransactionId());
            wxTrade.setTradeType(transaction.getTradeType().name());
            wxTrade.setCurrency(transaction.getAmount().getCurrency());
            wxTrade.setPayerTotal(transaction.getAmount().getPayerTotal());
            wxTrade.setPayerOpenId(transaction.getPayer().getOpenid());
            wxTrade.setBankType(transaction.getBankType());
            wxTrade.setSuccessTime(new Date(DateUtil.parse(transaction.getSuccessTime()).getTime()));
            wxTradeRepository.save(wxTrade);
        }
        return new CommonResponse<>("200", "查询本地订单成功", wxTrade);
    }

    @GetMapping("/closeTrade/{merchantId}")
    public CommonResponse<String> closeTrade(@PathVariable String merchantId,
                                             @RequestParam String outTradeNo) {
        WXNativeService wxNativeService = wxNativeServiceMgr.getWXNativeService(merchantId);
        wxNativeService.closeOrderByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "关闭订单成功", outTradeNo);
    }

    /**
     * 预支付请求体
     */
    @Data
    public static class PrepayRequest {
        /**
         * 应用ID
         */
        private String appId;
        
        /**
         * 商户订单号（用户端自己生成）
         */
        private String outTradeNo;
        
        /**
         * 订单描述
         */
        private String description;
        
        /**
         * 订单金额，单位：分
         */
        private int totalFee;
    }

}