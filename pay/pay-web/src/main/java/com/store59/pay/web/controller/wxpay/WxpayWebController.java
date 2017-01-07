/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.wxpay;

import javax.validation.Valid;

import com.store59.kylin.common.model.Result;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.WxpayTradeStatusEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.model.wxpay.*;
import com.store59.pay.service.config.PayConfig;
import com.store59.pay.service.config.WxpayConfig;
import com.store59.pay.service.wxpay.WxpayWebPayService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.web.converter.WxpayForm2WxpayWebPayRequestConverter;
import com.store59.pay.web.form.wxpay.WxpayWebForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.web.form.wxpay.WxpayForm;
import com.store59.pay.service.helper.WxpayWebHelper;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URLEncoder;

/**
 * 微信网页支付控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月4日
 * @since 1.0
 */
@Controller
@RequestMapping("/wxpay")
public class WxpayWebController {
    private static final Logger logger = LoggerFactory.getLogger(WxpayWebController.class);

    /** 若订单已支付, 在returnUrl后加上下面的Url. */
    private final static String ORDERPAID = "/paid";

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private WxpayConfig wxpayConfig;

    @Autowired
    private WxpayWebHelper      wxpayWebHelper;

    @Autowired
    private WxpayWebPayService wxpayWebPayService;

    @Value("${url.wechat-openid}")
    private String openidUrl;

    /**
     * 前端直接重定向到该方法, 发起微信支付(仅对接入了订单系统的业务有效)
     * @param wxpayWebForm
     * @param result
     * @return
     */
    @RequestMapping(value = "/web/pay", method = RequestMethod.GET)
    public ModelAndView webPay(@Valid WxpayWebForm wxpayWebForm,
                               BindingResult result,
                               ModelMap modelMap) {
        if (result.hasErrors()) {
            logger.error("【微信h5支付】, 参数错误: {}", result.getAllErrors());
            return new ModelAndView("error");
        }

        //如果openid为空, 重定向到另一个链接
        if (StringUtils.isBlank(wxpayWebForm.getOpenid())) {
            logger.info("【微信h5支付】openid为空");

            String redirectUrl = buildToOpenidUrl(wxpayWebForm);
            logger.info("【微信h5支付】将要重定向的完整url={}", redirectUrl);
            return new ModelAndView("redirect:" + redirectUrl);
        }
        logger.info("【微信h5支付】获取到openid={}", wxpayWebForm.getOpenid());

        WxpayWebPayRequest wxpayWebPayRequest = new WxpayWebPayRequest();
        BeanUtils.copyProperties(wxpayWebForm, wxpayWebPayRequest);
        //从openid系统接收的是returnAppUrl参数, 转换成returnUrl参数
        wxpayWebPayRequest.setReturnUrl(wxpayWebForm.getReturnAppUrl());

        //从订单系统里查询, 获取预支付参数
        Result<WxpayWebPayRequest> buildWxpayWebPayRequestResult = wxpayWebPayService.buildWxpayWebPayRequest(wxpayWebPayRequest);
        //是否已支付
        if (buildWxpayWebPayRequestResult.getStatus() == BizResultCodeEnum.ORDER_PAY_STATUS_FINISHED.getCode()) {
            return new ModelAndView("redirect:" + wxpayWebPayRequest.getReturnUrl() + ORDERPAID);
        }
        if (!buildWxpayWebPayRequestResult.isSuccess()) {
            logger.error("【微信h5支付】获取预支付参数失败, request={}, result={}",
                    JsonUtil.getJsonFromObject(wxpayWebPayRequest),
                    JsonUtil.getJsonFromObject(buildWxpayWebPayRequestResult));
            modelMap.put("msg", buildWxpayWebPayRequestResult.getMsg());
            return new ModelAndView("error");
        }
        wxpayWebPayRequest = buildWxpayWebPayRequestResult.getData();

        return getPrePayView(wxpayWebPayRequest);
    }

    /**
     * h5支付时获取预支付订单,直接展示web页面
     * @param wxpayForm
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView pay(@Valid WxpayForm wxpayForm, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("【微信h5支付】, 参数错误: {}", result.getAllErrors());
            return new ModelAndView("error");
        }
        WxpayWebPayRequest wxpayWebPayRequest = ModelConverterUtils.convert(wxpayForm, WxpayWebPayRequest.class);

        return getPrePayView(wxpayWebPayRequest);
    }

    /**
     * 获取预支付信息, 返回给前端界面
     * @param wxpayWebPayRequest
     * @return
     */
    private ModelAndView getPrePayView(WxpayWebPayRequest wxpayWebPayRequest) {
        ModelMap modelMap = new ModelMap();
        Result<WxpayUnifiedOrderResponse> serviceResult;

        //是否是服务商版
        if (wxpayConfig.isUseServerModel()) {
            WxpayServerUnifiedOrderRequest request = wxpayWebHelper.buildWxpayServerUnifiedOrderRequestForH5(wxpayWebPayRequest);
            serviceResult = wxpayWebPayService.requestPrepayId(request, PayChannelEnum.WXPAY_JSAPI);
        }else {
            WxpayUnifiedOrderRequest request = wxpayWebHelper.buildWxpayUnifiedOrderRequestForH5(wxpayWebPayRequest);
            serviceResult = wxpayWebPayService.requestPrepayId(request, PayChannelEnum.WXPAY_JSAPI);
        }

        if (!serviceResult.isSuccess()) {
            modelMap.put("msg", serviceResult.getMsg());
            return new ModelAndView("error", modelMap);
        }
        WxpayUnifiedOrderResponse response = serviceResult.getData();

        if (response == null || !response.isSuccess()) {
            //判断订单是否已支付
            if (StringUtils.equals(response.getErrCode(), WxpayTradeStatusEnum.ORDERPAID.toString())) {
                logger.warn("【微信h5支付】生成预支付订单失败: 订单已支付, request={}, responseData={} ", JsonUtil.getJsonFromObject(wxpayWebPayRequest), response);
                logger.info("要重定向的url={}", wxpayWebPayRequest.getReturnUrl() + ORDERPAID);
                return new ModelAndView("redirect:" + wxpayWebPayRequest.getReturnUrl() + ORDERPAID);
            }

            logger.error("【微信h5支付】生成预支付订单失败：request={}, responseData={} ", JsonUtil.getJsonFromObject(wxpayWebPayRequest), response);

            modelMap.put("msg", response.getReturnMsg());
            return new ModelAndView("error", modelMap);
        }

        WxpayWebPrePayRequest wxpayWebRequest = wxpayWebHelper.buildWxpayWebPrePay(response, wxpayWebPayRequest);

        logger.info("【微信h5支付】成功生成预支付订单,结果:{}", JsonUtil.getJsonFromObject(wxpayWebRequest));

        modelMap.put("data", wxpayWebRequest);

        return new ModelAndView("wxpay", modelMap);
    }

    /**
     * 构建重定向到openid系统的url
     * @param wxpayWebForm
     * @return
     */
    private String buildToOpenidUrl(WxpayWebForm wxpayWebForm) {
        return openidUrl + "/openid?mpName=" +
                wxpayConfig.getMpName() +
                "&orderId=" + wxpayWebForm.getOrderId() +
                "&returnUrl=" + URLEncoder.encode(payConfig.getUrl() + "/wxpay/web/pay") +
                (StringUtils.isBlank(wxpayWebForm.getReturnUrl()) ? "" : "&returnAppUrl=" + URLEncoder.encode(wxpayWebForm.getReturnUrl()));
    }

}
