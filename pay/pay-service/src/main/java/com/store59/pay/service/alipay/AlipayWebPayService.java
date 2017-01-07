/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.alipay;

import com.store59.dto.common.order.OrderPayStatusEnum;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.pay.model.alipay.AlipayBodyField;
import com.store59.pay.model.alipay.AlipayWebPayRequest;
import com.store59.pay.model.constants.AlipayConstants;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.service.config.AlipayConfig;
import com.store59.pay.service.config.PayConfig;
import com.store59.pay.service.order.OrderSystemService;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.service.util.ServiceResultUtil;
import com.store59.pay.service.wxpay.WxpayWebPayService;
import com.store59.pay.util.http.NameValuePairUtils;
import com.store59.pay.util.math.MathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝web端发起支付
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/4
 * @since 1.0
 */
@Service
public class AlipayWebPayService {
    private static final Logger logger = LoggerFactory.getLogger(AlipayWebPayService.class);


    @Autowired
    private PayConfig payConfig;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private SignatureService signatureService;

    /**
     * 支付宝web发起支付, 构造发起支付的url
     * @param alipayWebPayRequest
     * @return
     * @throws Exception
     */
    public Result pay(AlipayWebPayRequest alipayWebPayRequest) throws Exception {

        // 要发送给支付宝的参数
        Map<String, String> requestMap = new HashMap<>();
        //body字段,里面要放两个url
        AlipayBodyField alipayBodyField = new AlipayBodyField();
        //支付方式
        PayChannelEnum payChannel = PayChannelEnum.ALIPAY_PC;

        //是否是手机网站支付
        if (alipayWebPayRequest.getType() != null &&
                alipayWebPayRequest.getType().equals(PayChannelEnum.ALIPAY_WAP.getShortName())) {
            requestMap.put("service", "alipay.wap.create.direct.pay.by.user");
            payChannel = PayChannelEnum.ALIPAY_WAP;
        }else{
            requestMap.put("service", "create_direct_pay_by_user");
        }
        requestMap.put("partner", alipayConfig.getPartner());
        requestMap.put("_input_charset", alipayConfig.getInputCharset());
        requestMap.put("out_trade_no", alipayWebPayRequest.getOrderId());
        requestMap.put("subject", alipayWebPayRequest.getFoodName());
        requestMap.put("payment_type", "1");
        requestMap.put("total_fee", String.valueOf(alipayWebPayRequest.getMoney()));
        requestMap.put("seller_id", alipayConfig.getPartner());
        requestMap.put("notify_url", payChannel.getRequestUrl(payConfig.getUrl(), "notify")); // 异步通知url

        //如果有应用的notify_url
        if (StringUtils.isNotBlank(alipayWebPayRequest.getNotifyUrl())) {
            alipayBodyField. setAppNotifyUrl(alipayWebPayRequest.getNotifyUrl());
        }

        // 如果传过来的参数中有returnUrl，此url是支付完成要跳转到的应用的url,有才传递给支付宝
        if (StringUtils.isNotBlank(alipayWebPayRequest.getReturnUrl())) {
            requestMap.put("return_url", payChannel.getRequestUrl(payConfig.getUrl(), "return") + ""); // 同步通知url
            alipayBodyField.setAppReturnUrl(alipayWebPayRequest.getReturnUrl());
        }
        //传给支付宝的body参数
        requestMap.put("body", JsonUtil.getJsonFromObject(alipayBodyField));

        requestMap.put("sign", signatureService.sign(requestMap, payChannel));
        requestMap.put("sign_type", alipayConfig.getSignType());

        String url = new URIBuilder(AlipayConstants.ALIPAY_GATEWAY_NEW).setParameters(NameValuePairUtils.convert(requestMap)).toString();

        return ResultHelper.genResultWithSuccess(url);
    }
}
