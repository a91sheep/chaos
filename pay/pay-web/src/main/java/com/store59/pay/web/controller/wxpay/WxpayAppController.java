/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.wxpay;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.wxpay.WxpayAppPrepayRequest;
import com.store59.pay.model.wxpay.WxpayServerUnifiedOrderRequest;
import com.store59.pay.model.wxpay.WxpayUnifiedOrderResponse;
import com.store59.pay.model.wxpay.WxpayWebPayRequest;
import com.store59.pay.service.wxpay.WxpayWebPayService;
import com.store59.pay.web.form.wxpay.WxpayPrepayForm;
import com.store59.pay.service.helper.WxpayWebHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 微信支付app使用的控制器
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
@RestController
@RequestMapping("/wxpay")
public class WxpayAppController {
    private static final Logger logger = LoggerFactory.getLogger(WxpayWebController.class);

    @Autowired
    private WxpayWebHelper wxpayWebHelper;

    @Autowired
    private WxpayWebPayService wxpayWebPayService;

    /**
     * app获取预支付订单
     * @param wxpayPrepayForm
     * @param result
     */
    @RequestMapping(value = "/prepay_id", method = RequestMethod.POST)
    @ResponseBody
    public Result<WxpayAppPrepayRequest> prepayId(@Valid WxpayPrepayForm wxpayPrepayForm,
                                                  BindingResult result){
        logger.info("【微信支付获取预支付订单】wxpayPrepayForm={}", JsonUtil.getJsonFromObject(wxpayPrepayForm));
        if (result.hasErrors()) {
            logger.error("【微信支付获取预支付订单】, 参数错误: {}", result.getAllErrors());
        }

        WxpayWebPayRequest wxpayWebPayRequest = new WxpayWebPayRequest();
        BeanUtils.copyProperties(wxpayPrepayForm, wxpayWebPayRequest);
        //元转换为分
        wxpayWebPayRequest.setMoney(wxpayPrepayForm.getMoney().multiply(new BigDecimal(100)).intValue());

        WxpayServerUnifiedOrderRequest request = wxpayWebHelper.buildWxpayUnifiedOrderRequestForApp(wxpayWebPayRequest);

        //网络请求
        Result<WxpayUnifiedOrderResponse> serviceResult = wxpayWebPayService.requestPrepayId(request, PayChannelEnum.WXPAY_APP);
        if (!serviceResult.isSuccess()) {
            return ResultHelper.genResult(serviceResult.getStatus(), serviceResult.getMsg());
        }
        WxpayUnifiedOrderResponse response = serviceResult.getData();

        //格式化数据
        WxpayAppPrepayRequest wxpayAppPrepayRequest = wxpayWebHelper.buildWxpayAppPrepay(response);

        return ResultHelper.genResultWithSuccess(wxpayAppPrepayRequest);
    }
}
