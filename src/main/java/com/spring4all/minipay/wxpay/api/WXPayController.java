package com.spring4all.minipay.wxpay.api;

import cn.hutool.core.date.DateUtil;
import com.spring4all.minipay.common.CommonResponse;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.spring4all.minipay.wxpay.service.WXNativeService;
import com.spring4all.minipay.wxpay.utils.QRCodeUtils;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Slf4j
@RestController
@RequestMapping("/api/wxpay")
@AllArgsConstructor
public class WXPayController extends WXPayBaseController {

    private final WXTradeRepository wxTradeRepository;

    private WXNativeService wxNativeService;

    /**
     * 下单接口，获取微信支付的链接
     *
     * @param outTradeNo，商户订单号（用户端自己生成）
     * @param description，订单描述
     * @param totalFee，单位：分
     * @return
     * @throws Exception
     */
    @GetMapping("/prepay")
    public CommonResponse<String> prepay(@RequestParam String outTradeNo,
                                         @RequestParam String description,
                                         @RequestParam int totalFee) throws Exception {
        log.info("预支付：outTradeNo = {}, skuName = {}, totalFee = {}", outTradeNo, description, totalFee);
        String codeURl = wxNativeService.preNativePay(outTradeNo, description, totalFee);
        log.info("预支付：outTradeNo = {}, codeURl = {}", outTradeNo, codeURl);
        return new CommonResponse<String>("200", "预支付", codeURl);
    }

    /**
     * 获取支付二维码图片
     * 请求例子：wxpay/qrcode?codeUrl=weixin://wxpay/bizpayurl?pr=eJuVi9pzz
     * 其中，codeUrl通过调用 /wxpay/prepay 接口获取
     *
     * @param codeUrl
     * @param response
     * @throws Exception
     */
    @GetMapping("/qrcode")
    public void qrcode(@RequestParam("codeUrl") String codeUrl,
                       HttpServletResponse response) throws Exception {
        log.info("生成支付二维码, codeUrl = {}", codeUrl);
        response.setContentType("image/png");
        QRCodeUtils.writeToStream(codeUrl, response.getOutputStream(), 300, 300);
    }

    /**
     * 查询微信支付订单状态，并更新本地状态
     *
     * @param outTradeNo
     * @return
     */
    @GetMapping("/queryWXPayTrade")
    public CommonResponse<Transaction> queryWXPayTrade(@RequestParam String outTradeNo) {
        log.info("查询微信支付订单 outTradeNo: {}", outTradeNo);
        Transaction t = wxNativeService.queryWXPayTradeByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "查询成功：" + outTradeNo, t);
    }

    @GetMapping("/queryLocalTrade")
    public CommonResponse<WXTrade> queryLocalTrade(@RequestParam String outTradeNo) {
        log.info("查询本地支付订单 outTradeNo: {}", outTradeNo);
        WXTrade t = wxNativeService.queryLocalTradeByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "查询本地订单成功", t);
    }

    @GetMapping("/queryTrade")
    public CommonResponse<WXTrade> queryTrade(@RequestParam String outTradeNo) {
        log.info("查询本地订单和微信订单，返回最新状态: {}", outTradeNo);
        WXTrade wxTrade = wxNativeService.queryLocalTradeByOutTradeNo(outTradeNo);
        Transaction transaction = wxNativeService.queryWXPayTradeByOutTradeNo(outTradeNo);
        if(!wxTrade.getTradeState().equals(transaction.getTradeState().name())) {
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

    @GetMapping("/closeTrade")
    public CommonResponse<String> closeTrade(@RequestParam String outTradeNo) {
        wxNativeService.closeOrderByOutTradeNo(outTradeNo);
        return new CommonResponse<>("200", "关闭订单成功", outTradeNo);
    }

}