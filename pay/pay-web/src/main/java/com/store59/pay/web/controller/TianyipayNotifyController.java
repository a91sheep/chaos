package com.store59.pay.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.store59.kylin.utils.JsonUtil;
import com.store59.pay.model.constants.TianyipayConstants;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.enums.PayPlatformEnum;
import com.store59.pay.model.enums.TianyipayTradeStatusEnum;
import com.store59.pay.model.event.PayEvent;
import com.store59.pay.service.notify.PayNotifyService;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.converter.ModelConverterUtils;
import com.store59.pay.web.form.TianyipayAsyncNotifyForm;
import com.store59.pay.web.utils.ServletRequestUtils;

/**
 * 翼支付异步通知类
 * 目前只处理自己app上支付完后的异步通知
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 18:38
 */
@RestController
@RequestMapping(value = "/tianyipay")
public class TianyipayNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(TianyipayNotifyController.class);

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private PayNotifyService payNotifyService;

    /**
     * 翼支付app异步回调.
     *
     * @param request
     * @param notifyForm
     * @param result
     * @return
     * @throws Exception
     */
    @Deprecated
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String web(HttpServletRequest request, @Valid TianyipayAsyncNotifyForm notifyForm, BindingResult result) throws Exception {
        return asyncNotify(request, PayChannelEnum.TIANYI_APP.getShortName(), notifyForm, result);
    }

    @RequestMapping(value = "/{channel}/notify", method = RequestMethod.POST)
    public String asyncNotify(HttpServletRequest request,
                              @PathVariable String channel,
                              @Valid TianyipayAsyncNotifyForm notifyForm,
                              BindingResult result)
            throws Exception {

        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);
        logger.info("【翼支付异步回调】接收到回调，参数为：{}", parameterMap);

        //检查传过来的参数是否有误, 少传
        if (result.hasErrors()) {
            logger.error("【翼支付异步回调】参数有误：notifyForm={}，errors={}", notifyForm, result.getAllErrors());
            return result.getFieldError().getDefaultMessage();
        }

        //获取翼支付的支付方式
        PayChannelEnum payChannel = PayChannelEnum.getEnum(PayPlatformEnum.TIANYI, channel);
        logger.debug("【翼支付异步回调】本次支付方式: {}", JsonUtil.getJsonFromObject(payChannel));
        if (payChannel == null) {
            logger.error("【翼支付异步回调】翼支付渠道不支持：{}", channel);
            return TianyipayConstants.FAIL;
        }

        //验证签名
        if (!signatureService.verify(parameterMap, payChannel)) {
            logger.error("【翼支付异步回调】验证签名失败，参数为：{}", parameterMap);
            return TianyipayConstants.FAIL;
        }

        //交易是否成功
        if(!StringUtils.equals(notifyForm.getRETNCODE(), TianyipayTradeStatusEnum.SUCCESS.getCode())){
            logger.error("【翼支付异步回调】交易状态不成功, result={}", notifyForm.getRETNCODE());
            return TianyipayConstants.FAIL;
        }

        //发送成功通知
        //1. 获取需要的内容
        String notifyUrl = notifyForm.getATTACH();
        PayEvent payEvent = ModelConverterUtils.convert(notifyForm, PayEvent.class);
        payEvent.setBizResult(BizResultCodeEnum.SUCCESS);
        payEvent.setPayType(PayChannelEnum.TIANYI_APP.getCode());

        //2. 通知应用并且返回消息给翼支付平台
        //返回给翼支付平台成功消息, 写入格式为UPTRANSEQ_XXXXXX的字符串, XXXXXX为翼支付网关平台发送过去的翼支付网关平台交易流水号
        String responseSuccessMsg = TianyipayConstants.SUCCESS + notifyForm.getUPTRANSEQ();
        return payNotifyService.sendEvent(notifyUrl, payEvent) ? responseSuccessMsg : TianyipayConstants.FAIL;
    }


}
