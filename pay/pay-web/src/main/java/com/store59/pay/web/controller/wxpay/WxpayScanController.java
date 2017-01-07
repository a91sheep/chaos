/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.wxpay;

import java.util.Map;

import javax.validation.Valid;

import com.store59.kylin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store59.pay.model.constants.WxpayConstants;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.wxpay.WxpayScanRequest;
import com.store59.pay.model.wxpay.WxpayScanResponse;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.http.WebServiceUtils;
import com.store59.pay.util.http.XmlWebServiceHandler;
import com.store59.pay.util.lang.XmlUtils;
import com.store59.pay.util.oxm.OXMUtils;
import com.store59.pay.web.form.wxpay.WxpayScanForm;
import com.store59.pay.web.helper.WxpayScanHelper;
import com.store59.pay.web.model.WxpayScanResult;

/**
 * 微信刷卡支付控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月8日
 * @since 1.0
 */
@Controller
public class WxpayScanController {
    private static final Logger logger = LoggerFactory.getLogger(WxpayScanController.class);

    @Autowired
    private WxpayScanHelper     wxpayScanHelper;

    @Autowired
    private SignatureService    signatureService;

    /**
     * 微信刷卡支付.
     * 
     * @param form {@link WxpayScanForm}
     * @return {@link WxpayScanResult}
     */
    @RequestMapping(value = { "/wxpay/swipe_card", "/wxpay/scan/pay" }, method = RequestMethod.POST)
    @ResponseBody
    public WxpayScanResult scanPay(@Valid WxpayScanForm form, BindingResult result) throws Exception {
        logger.info("【微信刷卡支付】收到请求，参数为：{}", form);

        WxpayScanResult wxpayScanResult = new WxpayScanResult();
        if (result.hasErrors()) {
            wxpayScanResult.setBizResult(BizResultCodeEnum.PARAM_INVALID);
            return wxpayScanResult;
        }

        WxpayScanRequest wxpayScanRequest = wxpayScanHelper.buildWxpayScanRequest(form);
        String responseData = WebServiceUtils.post(WxpayConstants.SCAN_PAY_URL, wxpayScanRequest,
                new XmlWebServiceHandler<WxpayScanRequest, String>(String.class) {
                    @Override
                    protected String parseResponseData(String responseData) {
                        return responseData;
                    }
                });

        Map<String, String> parameters = XmlUtils.parse2Map(responseData);
        if (!signatureService.verify(parameters, PayChannelEnum.WXPAY_SCAN)) {
            logger.error("【微信刷卡支付】签名无效：request={}, responseData={} ", wxpayScanRequest, responseData);
            wxpayScanResult.setBizResult(BizResultCodeEnum.SIGN_INVALID);
            return wxpayScanResult;
        }

        WxpayScanResponse wxpayScanResponse = OXMUtils.unmarshal(responseData, WxpayScanResponse.class);

        //return_code=fail
        if (wxpayScanResponse == null || wxpayScanResponse.getReturnCode().equals(WxpayConstants.FAIL)) {
            logger.error("【微信刷卡支付】失败, return_code=fail: request={}, \nresponseData={} ", wxpayScanRequest, responseData);
            wxpayScanResult.setBizResult(BizResultCodeEnum.WXPAY_SCAN_RETURN_CODE_FAIL);
            return wxpayScanResult;
        }

        wxpayScanHelper.buildWxpayScanResult(wxpayScanResult, wxpayScanResponse);
        logger.info("【微信刷卡支付】返回给应用, result={}", JsonUtil.getJsonFromObject(wxpayScanResult));

        return wxpayScanResult;
    }

}
