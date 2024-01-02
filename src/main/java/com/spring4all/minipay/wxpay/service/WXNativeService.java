package com.spring4all.minipay.wxpay.service;

import com.spring4all.minipay.exception.TradeIsClosedException;
import com.spring4all.minipay.exception.TradeIsSuccessException;
import com.spring4all.minipay.wxpay.WXPayConfig;
import com.spring4all.minipay.wxpay.WXPayProperties;
import com.spring4all.minipay.wxpay.dao.WXNotificationRepository;
import com.spring4all.minipay.wxpay.dao.WXTradeRepository;
import com.spring4all.minipay.wxpay.entity.WXNotification;
import com.spring4all.minipay.wxpay.entity.WXTrade;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WXNativeService {

    private final WXTradeRepository wxTradeRepository;
    private final WXNotificationRepository wxNotificationRepository;

    private WXPayProperties wxPayProperties;
    private NativePayService nativePayService;
    private NotificationParser notificationParser;


    public WXNativeService(WXPayConfig wxPayConfig,
                           WXTradeRepository wXTradeRepository,
                           WXNotificationRepository wxNotificationRepository) {
        this.wxPayProperties = wxPayConfig.getWxPayProperties();

        this.nativePayService = new NativePayService.Builder().config(wxPayConfig.getConfig()).build();
        this.notificationParser = new NotificationParser(wxPayConfig.getConfig());

        log.info("微信支付配置初始化：成功！");

        this.wxTradeRepository = wXTradeRepository;
        this.wxNotificationRepository = wxNotificationRepository;
    }

    public String preNativePay(String outTradeNo, String description, int totalFee) {
        // 该商户订单号存在，同时商户订单还没有关闭，那么就直接返回codeUrl给商户扫码支付
        WXTrade _wxTrade = wxTradeRepository.findByOutTradeNo(outTradeNo);
        if (_wxTrade != null) {
            // 本地数据记录订单已经关闭或已经完成，则直接抛出异常
            switch(_wxTrade.getTradeState()) {
                case "CLOSED": throw new TradeIsClosedException();
                case "SUCCESS": throw new TradeIsSuccessException();
            }

            // 本地数据记录订单未关闭，插微信端是否已经关闭，如果已经关闭，更新本地，并抛出异常
            Transaction t = queryWXPayTradeByOutTradeNo(outTradeNo);
            if (t.getTradeState().equals(Transaction.TradeStateEnum.CLOSED)) {
                _wxTrade.setTradeState(t.getTradeState().name());
                _wxTrade.setTradeStateDesc(t.getTradeStateDesc());
                wxTradeRepository.save(_wxTrade);
                throw new TradeIsClosedException();
            }
            log.info("outTradeNo = {} 已存在，直接返回 codeUrl = {}", outTradeNo, _wxTrade.getCodeUrl());
            return _wxTrade.getCodeUrl();
        }

        // 商户订单号没问题，准备调用微信的与下单
        PrepayRequest request = new PrepayRequest();
        request.setAppid(wxPayProperties.getAppId());
        request.setMchid(wxPayProperties.getMerchantId());
        request.setNotifyUrl(wxPayProperties.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        request.setDescription(description);

        Amount amount = new Amount();
        amount.setTotal(totalFee);
        request.setAmount(amount);

        // 调用微信接口预下单
        PrepayResponse response = nativePayService.prepay(request); // 调用下单方法，得到应答
        String codeUrl = response.getCodeUrl(); // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        log.info("预支付 codeUrl = {}", codeUrl);

        // 查询微信支付的订单，获取完整信息，并存储到本地数据库
        Transaction t = queryWXPayTradeByOutTradeNo(outTradeNo);
        WXTrade wxTrade = new WXTrade();
        wxTrade.prepayUpdate(t);
        wxTrade.setOutTradeTitle(description);
        wxTrade.setCodeUrl(codeUrl);
        wxTradeRepository.save(wxTrade);

        return codeUrl;
    }

    /**
     * <h1>订单查询</h1>
     * 微信支付查询订单接口是一款为接入微信支付的商户提供实时订单查询服务的工具。
     * 通过调用该接口，商户可以快速了解订单的支付状态、交易金额、支付时间等详细信息，从而实现高效的订单管理。
     * 该产品适用于各类线上线下商户，帮助商户提升客户体验，实现业务增长
     * 产品为商户带来实时订单查询、异常订单处理等能力，为用户提供更加便捷的支付体验。
     *
     * <h2>功能特点</h2>
     * 实时查询：允许商户实时查询订单状态，获取订单的支付状态、支付金额等详细信息。
     * 核实信息：接口返回的数据包括订单号、交易金额、支付状态、支付时间等详细信息，方便商户进行订单管理。
     * 订单状态更新：查询订单接口返回的订单状态实时更新，方便开发者和商户进行订单管理。
     *
     * <h2>适用场景</h2>
     * 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知。
     * 调用支付接口后，返回系统错误或未知交易状态情况。
     * 调用付款码支付API，返回USERPAYING的状态。
     * 调用关单或撤销接口API之前，需确认支付状态。
     *
     * @param outTradeNo
     * @return
     */
    public Transaction queryWXPayTradeByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        queryRequest.setMchid(wxPayProperties.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);

        try {
            Transaction result = nativePayService.queryOrderByOutTradeNo(queryRequest);
            log.info("订单查询：{}", result);
            return result;
        } catch (ServiceException e) {
            // API返回失败, 例如ORDER_NOT_EXISTS
            log.error("code={}, message={}", e.getErrorCode(), e.getErrorMessage());
            log.error("reponse body={}", e.getResponseBody());
            throw e;
        }
    }

    /**
     * 查询本地数据库保存的订单数据，用来与微信端核对当前状态是否一致
     *
     * @param outTradeNo
     * @return
     */
    public WXTrade queryLocalTradeByOutTradeNo(String outTradeNo) {
        WXTrade wxTrade = wxTradeRepository.findByOutTradeNo(outTradeNo);
        return wxTrade;
    }

    /**
     * <h1>关闭订单 </h1>
     * 微信支付关闭订单接口是一款为商户提供订单关闭功能的 API 服务。
     * 适用于各类商户。通过关闭订单接口，商户可以在订单未支付时，主动关闭订单，释放库存或取消服务，帮助商户实现更灵活的订单管理。
     *
     * <h2>功能特点</h2>
     * 实时关闭：在订单未支付时，允许开发者和商户实时关闭订单，避免资源浪费。
     * 订单状态更新：关闭订单后，订单状态将实时更新，方便开发者和商户进行订单管理。
     *
     * <h2>适用场景</h2>
     * 超时未支付：当用户下单后未在规定时间内完成支付，商户可以通过关闭订单接口主动关闭订单，释放库存或取消服务。这有助于商户避免因长时间占用库存而导致其他用户无法购买的问题。
     * 订单支付失败：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付。
     *
     * @param outTradeNo 商户订单号
     */
    public void closeOrderByOutTradeNo(String outTradeNo) {
        // 调用微信支付关闭订单接口，提交关闭订单请求。
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(wxPayProperties.getMerchantId());
        closeRequest.setOutTradeNo(outTradeNo);
        nativePayService.closeOrder(closeRequest); // 方法没有返回值，意味着成功时API返回204 No Content

        // 再查询一下微信端的订单状态，如果是关闭的话，代表调用成功，更新本地数据库
        Transaction t = queryWXPayTradeByOutTradeNo(outTradeNo);
        if (t.getTradeState().equals(Transaction.TradeStateEnum.CLOSED)) {
            log.info("关闭商户订单: {} ", outTradeNo);
            WXTrade wxTrade = wxTradeRepository.findByOutTradeNo(outTradeNo);
            wxTrade.setTradeState(t.getTradeState().name());
            wxTrade.setTradeStateDesc(t.getTradeStateDesc());
            wxTradeRepository.save(wxTrade);
        } else {
            // TODO 订单没有成功关闭，给出提示
            log.error("订单关闭失败：" + outTradeNo);
        }
    }

    /**
     * 处理支付回调
     * <p>
     * 支付结果通知是指在用户完成支付后，微信支付系统通过异步回调的方式通知商户关于支付结果。
     * 商户需要在接收到支付结果通知后，对通知内容进行验证和处理，确保交易的正确性和安全性。
     * <p>
     * 支付结果通知包含以下信息：
     * - 通知内容：包括支付状态、订单号、支付金额等交易信息。
     * - 通知签名：请求头包含签名信息，商户需验证签名以确保通知内容的真实性。
     *
     * @param body
     * @param serialNumber
     * @param nonce
     * @param signature
     * @param signatureType
     * @param timestamp
     */
    public void processNotification(String body,
                                    String serialNumber, String nonce,
                                    String signature, String signatureType,
                                    String timestamp) {

        WXNotification wxNotification = new WXNotification();
        wxNotification.setBody(body);
        wxNotification.setSerialNumber(serialNumber);
        wxNotification.setNonce(nonce);
        wxNotification.setSignature(signature);
        wxNotification.setSignatureType(signatureType);
        wxNotification.setTimestamp(timestamp);
        wxNotification = wxNotificationRepository.save(wxNotification);

        // 验签、解密并转换成 Transaction
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serialNumber)
                .nonce(nonce)
                .signature(signature)
                .signType(signatureType)
                .timestamp(timestamp)
                .body(body)
                .build();
        Transaction t = this.notificationParser.parse(requestParam, Transaction.class);
        log.info("notify body parse = {}", t);

        wxNotification.setParse(t.toString());
        wxNotification.setOutTradeNo(t.getOutTradeNo());
        wxNotificationRepository.save(wxNotification);

        // 解析后更新本地订单数据
        WXTrade wxTrade = wxTradeRepository.findByOutTradeNo(t.getOutTradeNo());
        wxTrade.notificationUpdate(t);
        wxTradeRepository.save(wxTrade);

        // TODO 通知订阅方订单状态更新
        // TODO 应该支持多种方式：HTTP、RPC、MQ、WebSocket
    }

    // TODO 有时候支付回调没有成功，这个时候支付状态如何同步？

}
