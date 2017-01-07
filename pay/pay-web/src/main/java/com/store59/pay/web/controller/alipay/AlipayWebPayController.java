/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.alipay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.store59.dto.common.order.OrderPayStatusEnum;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.pay.model.alipay.AlipayWebPayRequest;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.service.alipay.AlipayWebPayService;
import com.store59.pay.service.order.OrderSystemService;
import com.store59.pay.service.util.ServiceResultUtil;
import com.store59.pay.web.form.alipay.AlipayWebPayForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.store59.kylin.common.model.Result;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.form.PayForm;
import com.store59.pay.web.model.ViewResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * 支付宝网页支付控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/alipay")
public class AlipayWebPayController {
    private static final Logger logger = LoggerFactory.getLogger(AlipayWebPayController.class);

    /** 若订单已支付, 在returnUrl后加上下面的Url. */
    private final static String ORDERPAID = "/paid";

    @Autowired
    private AlipayWebPayService alipayWebPayService;

    @Autowired
    OrderSystemService orderSystemService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    /**
     *  web端发起支付, 前端直接调用(仅适用于接入了订单中心的业务)
     * @param alipayWebPayForm
     * @param result
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/pay", method = RequestMethod.GET)
    public ModelAndView web(@Valid AlipayWebPayForm alipayWebPayForm,
                            BindingResult result,
                            ModelMap modelMap) throws Exception {
        AlipayWebPayRequest alipayWebPayRequest = new AlipayWebPayRequest();
        if (result.hasErrors()) {
            logger.error("【支付宝web端支付】参数有误: {}", result.getFieldError().getDefaultMessage());
            modelMap.put("msg", result.getFieldError().getDefaultMessage());
            return new ModelAndView("error", modelMap);
        }


        BeanUtils.copyProperties(alipayWebPayForm, alipayWebPayRequest);

        //查询订单
        Result<OrderDTO> orderResult = orderSystemService.queryOrder(alipayWebPayRequest.getOrderId());
        if (!orderResult.isSuccess()) {
            modelMap.put("msg", orderResult.getMsg());
            return new ModelAndView("error", modelMap);
        }
        OrderDTO orderDTO = orderResult.getData();

        //是否已支付
        if (orderDTO.getPayStatus() == OrderPayStatusEnum.FINISHED) {
            return new ModelAndView("redirect:" + alipayWebPayRequest.getReturnUrl() + ORDERPAID);
        }

        //金额转换, 分转换成元 除以100
        alipayWebPayRequest.setMoney(new BigDecimal(orderDTO.getPayAmount()).divide(new BigDecimal(100)));
        //付款标题
        alipayWebPayRequest.setFoodName("59store" + orderDTO.getType().getDesc() + "订单");
        Result serviceResult = alipayWebPayService.pay(alipayWebPayRequest);

        return new ModelAndView("redirect:" + serviceResult.getData());
    }

    /**
     * 发起支付宝网页支付请求.
     * 后端接口, 适合未接入订单中心的业务
     * 
     * @param payForm
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<Map<String, String>> pay(@Valid PayForm payForm, BindingResult result) throws Exception {
        AlipayWebPayRequest alipayWebPayRequest = new AlipayWebPayRequest();
        ViewResult<Map<String, String>> viewResult = new ViewResult<>();

        if (result.hasErrors()) {
            logger.error("【支付宝web端支付】参数有误: {}", result.getFieldError().getDefaultMessage());
            viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
            return viewResult.toResultIgnoreData();
        }

        BeanUtils.copyProperties(payForm, alipayWebPayRequest);
        Result serviceResult = alipayWebPayService.pay(alipayWebPayRequest);

        if (!serviceResult.isSuccess()) {
            viewResult.setResultCode(ViewResultCodeEnum.UNKNOWN_ERROR);
            return viewResult.toResult();
        }
        String url = (String) serviceResult.getData();

        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", url);
        viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
        viewResult.setData(urlMap);

        return viewResult.toResult();
    }


}
