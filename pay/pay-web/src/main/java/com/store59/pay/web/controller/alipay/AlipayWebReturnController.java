/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.alipay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.alipay.AlipayBodyField;
import com.store59.pay.model.constants.AlipayConstants;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.PayPlatformEnum;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.web.utils.ServletRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付宝同步返回
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/alipay")
public class AlipayWebReturnController {
    private static final Logger logger = LoggerFactory.getLogger(AlipayWebReturnController.class);

    @Autowired
    private SignatureService signatureService;

    /**
     * 支付宝同步回调.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    @ResponseBody
    public String syncReturn(HttpServletRequest request) throws Exception {
        return syncNotify(request, PayChannelEnum.ALIPAY_PC.getShortName());
    }

    /**
     * 支付宝同步回调.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{channel}/return", method = RequestMethod.GET)
    public String syncNotify(HttpServletRequest request, @PathVariable String channel) throws Exception {
        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);
        logger.info("【支付宝网页支付同步回调】收到参数为：{}", parameterMap);

        PayChannelEnum payChannel = PayChannelEnum.getEnum(PayPlatformEnum.ALIPAY, channel);
        if (payChannel == null) {
            logger.error("【支付宝同步回调】支付宝支付渠道不支持：{}", channel);
            return AlipayConstants.FAIL;
        }

        // 验证签名
        if (!signatureService.verify(parameterMap, payChannel)) {
            logger.error("【支付宝网页支付同步回调】验证签名失败，参数为：{}", parameterMap);
            return AlipayConstants.FAIL;
        }

        //从body参数里获取要跳转的url
        String body = request.getParameter("body");
        if(body == null || StringUtils.isBlank(body)){
            logger.error("【支付宝网页支付同步回调】收到body字段的值为null或空");
            return AlipayConstants.FAIL;
        }

        //判断支付宝同步回调是否成功
        String flag = "/fail";
        if(request.getParameter("is_success").equals("T")){
            flag = "/success";
        }
        AlipayBodyField alipayBodyField = JsonUtil.getObjectFromJson(body, new TypeReference<AlipayBodyField>(){});
        // 跳转至应用链接
        return "redirect:" + alipayBodyField.getAppReturnUrl() + flag;
    }
}
