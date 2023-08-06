package com.spring4all.minipay.wxpay.dto;

import lombok.Data;

/**
 * <h1>错误信息</h1>
 *
 * 微信支付API v3使用HTTP状态码来表示请求处理的结果。
 *
 * 处理成功的请求，如果有应答的消息体将返回200，若没有应答的消息体将返回204。
 * 已经被成功接受待处理的请求，将返回202。
 * 请求处理失败时，如缺少必要的入参、支付时余额不足，将会返回4xx范围内的错误码。
 * 请求处理时发生了微信支付侧的服务系统错误，将返回500/501/503的状态码。这种情况比较少见。
 *
 * <h1>错误码和错误提示</h1>
 * 当请求处理失败时，除了HTTP状态码表示错误之外，API将在消息体返回错误相应说明具体的错误原因。
 *
 * code：详细错误码
 * message：错误描述，使用易理解的文字表示错误的原因。
 * field: 指示错误参数的位置。当错误参数位于请求body的JSON时，填写指向参数的JSON Pointer 。当错误参数位于请求的url或者querystring时，填写参数的变量名。
 * value:错误的值
 * issue:具体错误原因
 *
 * {
 *   "code": "PARAM_ERROR",
 *   "message": "参数错误",
 *   "detail": {
 *     "field": "/amount/currency",
 *     "value": "XYZ",
 *     "issue": "Currency code is invalid",
 *     "location" :"body"
 *   }
 * }
 *
 * 相关文档：https://pay.weixin.qq.com/wiki/doc/apiv3_partner/wechatpay/wechatpay2_0.shtml
 */
@Data
public class WXError {

    private String code;
    private String message;

    private WXErrorDetail detail;

}
