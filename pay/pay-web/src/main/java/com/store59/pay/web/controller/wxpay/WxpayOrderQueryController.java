/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.web.controller.wxpay;

import java.util.Map;

import javax.validation.Valid;

import com.store59.pay.model.enums.PayChannelEnum;
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
import com.store59.pay.model.wxpay.WxpayOrderQueryRequest;
import com.store59.pay.model.wxpay.WxpayOrderQueryResponse;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.util.http.WebServiceUtils;
import com.store59.pay.util.http.XmlWebServiceHandler;
import com.store59.pay.util.lang.XmlUtils;
import com.store59.pay.util.oxm.OXMUtils;
import com.store59.pay.web.form.wxpay.WxpayOrderQueryForm;
import com.store59.pay.web.helper.WxpayOrderQueryHelper;
import com.store59.pay.web.model.WxpayOrderQueryResult;

/**
 * 微信支付查询订单控制器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月15日
 * @since 1.0
 */
@Controller
public class WxpayOrderQueryController {
    private static final Logger   logger = LoggerFactory.getLogger(WxpayOrderQueryController.class);

    @Autowired
    private WxpayOrderQueryHelper wxpayOrderQueryHelper;

    @Autowired
    private SignatureService      signatureService;

    /**
     * 查询微信支付订单.
     * 
     * @param form {@link WxpayOrderQueryForm}
     * @param result {@link BindingResult}
     * @return {@link WxpayOrderQueryResult}
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/wxpay/order_query", method = RequestMethod.POST)
    public WxpayOrderQueryResult orderQuery(@Valid WxpayOrderQueryForm form, BindingResult result) throws Exception {
        logger.info("收到【查询微信支付订单】请求，参数为：{}", form);

        WxpayOrderQueryResult wxpayOrderQueryResult = new WxpayOrderQueryResult();
        wxpayOrderQueryResult.setPayType(form.getPayType());
        if (result.hasErrors()) {
            wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.PARAM_INVALID);
            return wxpayOrderQueryResult;
        }

        WxpayOrderQueryRequest wxpayOrderQueryRequest = wxpayOrderQueryHelper.buildWxpayOrderQueryRequest(form);
        String responseData = WebServiceUtils.post(WxpayConstants.ORDER_QUERY_URL, wxpayOrderQueryRequest,
                new XmlWebServiceHandler<WxpayOrderQueryRequest, String>(String.class) {
                    @Override
                    protected String parseResponseData(String responseData) {
                        return responseData;
                    }
                });

        Map<String, String> parameters = XmlUtils.parse2Map(responseData);
        if (!signatureService.verify(parameters, PayChannelEnum.WXPAY_SCAN)) {
            logger.error("【查询微信支付订单】签名无效：request={}, responseData={} ", wxpayOrderQueryRequest, responseData);
            wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.SIGN_INVALID);
            return wxpayOrderQueryResult;
        }

        WxpayOrderQueryResponse wxpayOrderQueryResponse = OXMUtils.unmarshal(responseData, WxpayOrderQueryResponse.class);

        //return_code != success
        if (wxpayOrderQueryResponse == null || !wxpayOrderQueryResponse.getReturnCode().equals(WxpayConstants.SUCCESS)) {
            logger.error("【查询微信支付订单】返回失败, return_code=fail：request={}, responseData={} ", wxpayOrderQueryRequest, responseData);
            wxpayOrderQueryResult.setBizResult(BizResultCodeEnum.ORDER_QUERY_WXPAY_SCAN_RETURN_CODE_FAIL);

            return wxpayOrderQueryResult;
        }

        wxpayOrderQueryHelper.buildWxpayOrderQueryResult(wxpayOrderQueryResult, wxpayOrderQueryResponse);
        return wxpayOrderQueryResult;
    }

}
