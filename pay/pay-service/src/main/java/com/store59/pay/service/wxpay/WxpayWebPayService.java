/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.wxpay;

import com.store59.dto.common.order.OrderPayStatusEnum;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.pay.model.constants.WxpayConstants;
import com.store59.pay.model.enums.BizResultCodeEnum;
import com.store59.pay.model.enums.PayChannelEnum;
import com.store59.pay.model.wxpay.WxpayServerUnifiedOrderRequest;
import com.store59.pay.model.wxpay.WxpayUnifiedOrderRequest;
import com.store59.pay.model.wxpay.WxpayUnifiedOrderResponse;
import com.store59.pay.model.wxpay.WxpayWebPayRequest;
import com.store59.pay.service.order.OrderSystemService;
import com.store59.pay.service.signature.SignatureService;
import com.store59.pay.service.util.ServiceResultUtil;
import com.store59.pay.util.http.WebServiceUtils;
import com.store59.pay.util.http.XmlWebServiceHandler;
import com.store59.pay.util.lang.XmlUtils;
import com.store59.pay.util.oxm.OXMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信web支付
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/5
 * @since 1.0
 */
@Service
public class WxpayWebPayService {
    private static final Logger logger = LoggerFactory.getLogger(WxpayWebPayService.class);

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private OrderSystemService orderSystemService;

    /**
     * 填充获取h5支付需要的参数(仅支持接入了订单系统的业务)
     * @param wxpayWebPayRequest
     * @return
     */
    public Result<WxpayWebPayRequest> buildWxpayWebPayRequest(WxpayWebPayRequest wxpayWebPayRequest) {
        //1. 获取订单数据
        Result<OrderDTO> result = orderSystemService.queryOrder(wxpayWebPayRequest.getOrderId());
        if (!result.isSuccess()) {
            return ResultHelper.genResult(result.getStatus(), result.getMsg());
        }
        OrderDTO orderDTO = result.getData();

        //2. 是否已支付
        if (orderDTO.getPayStatus() == OrderPayStatusEnum.FINISHED) {
            logger.error("【订单service】订单已支付, orderId={}, result={}", wxpayWebPayRequest.getOrderId(), result);
            return ServiceResultUtil.toResult(BizResultCodeEnum.ORDER_PAY_STATUS_FINISHED);
        }

        //3. 填充wxpayWebPayRequest
        wxpayWebPayRequest.setFoodName("59store" + orderDTO.getType().getDesc() + "订单");
        wxpayWebPayRequest.setMoney(orderDTO.getPayAmount());

        return ResultHelper.genResultWithSuccess(wxpayWebPayRequest);
    }

    /**
     * 网络请求请求微信获取预支付信息
     * 服务商版
     * @param request
     * @return
     */
    public Result<WxpayUnifiedOrderResponse> requestPrepayId(WxpayServerUnifiedOrderRequest request, PayChannelEnum payChannelEnum) {
        logger.info("【微信支付获取预支付订单】发送给微信服务器的数据, request= {}", JsonUtil.getJsonFromObject(request));
        String responseData = WebServiceUtils.post(WxpayConstants.ORDER_URL, request,
                new XmlWebServiceHandler<WxpayServerUnifiedOrderRequest, String>(String.class) {
                    @Override
                    protected String parseResponseData(String responseData) {
                        return responseData;
                    }
                });
        logger.info("【微信支付获取预支付订单】从微信服务器获取预支付信息, responseData={}", responseData);

        Map<String, String> parameters = XmlUtils.parse2Map(responseData);
        if (!signatureService.verify(parameters, payChannelEnum)) {
            logger.error("【微信支付获取预支付订单】从微信获取数据验证签名,签名无效：request={}, responseData={} ", JsonUtil.getJsonFromObject(request), responseData);

            return ServiceResultUtil.toResult(BizResultCodeEnum.UNKNOWN_EXCEPTION);
        }
        WxpayUnifiedOrderResponse response = OXMUtils.unmarshal(responseData, WxpayUnifiedOrderResponse.class);

        //returnCode
        if (!response.getReturnCode().equals(WxpayConstants.SUCCESS)) {
            logger.error("【微信支付获取预支付订单】returnCode=fail, response={}", JsonUtil.getJsonFromObject(response));
            return ResultHelper.genResult(BizResultCodeEnum.PREPAY_RETURN_CODE_FAIL.getCode(), response.getReturnMsg());
        }
        //resultCode
        if (!response.getResultCode().equals(WxpayConstants.SUCCESS)) {
            logger.error("【微信支付获取预支付订单】resultCode=fail, response={}", JsonUtil.getJsonFromObject(response));
            return ServiceResultUtil.toResult(BizResultCodeEnum.PREPAY_RESULT_CODE_FAIL);
        }
        return ResultHelper.genResultWithSuccess(response);
    }

    /**
     * 网络请求请求微信获取预支付信息
     * 普通版
     * @param request
     * @return
     */
    public Result<WxpayUnifiedOrderResponse> requestPrepayId(WxpayUnifiedOrderRequest request, PayChannelEnum payChannelEnum) {
        logger.info("【微信支付获取预支付订单】发送给微信服务器的数据, request= {}", JsonUtil.getJsonFromObject(request));
        String responseData = WebServiceUtils.post(WxpayConstants.ORDER_URL, request,
                new XmlWebServiceHandler<WxpayUnifiedOrderRequest, String>(String.class) {
                    @Override
                    protected String parseResponseData(String responseData) {
                        return responseData;
                    }
                });
        logger.info("【微信支付获取预支付订单】从微信服务器获取预支付信息, responseData={}", responseData);

        Map<String, String> parameters = XmlUtils.parse2Map(responseData);
        if (!signatureService.verify(parameters, payChannelEnum)) {
            logger.error("【微信支付获取预支付订单】从微信获取数据验证签名,签名无效：request={}, responseData={} ", JsonUtil.getJsonFromObject(request), responseData);

            return ServiceResultUtil.toResult(BizResultCodeEnum.UNKNOWN_EXCEPTION);
        }
        WxpayUnifiedOrderResponse response = OXMUtils.unmarshal(responseData, WxpayUnifiedOrderResponse.class);
        logger.info("unmarshal response={}", response);
        //returnCode
        if (!response.getReturnCode().equals(WxpayConstants.SUCCESS)) {
            logger.error("【微信支付获取预支付订单】returnCode=fail, response={}", JsonUtil.getJsonFromObject(response));
            return ResultHelper.genResult(BizResultCodeEnum.PREPAY_RETURN_CODE_FAIL.getCode(), response.getReturnMsg());
        }
        //resultCode
        if (!response.getResultCode().equals(WxpayConstants.SUCCESS)) {
            logger.error("【微信支付获取预支付订单】resultCode=fail, response={}", JsonUtil.getJsonFromObject(response));
            return ServiceResultUtil.toResult(BizResultCodeEnum.PREPAY_RESULT_CODE_FAIL);
        }
        return ResultHelper.genResultWithSuccess(response);
    }

}
