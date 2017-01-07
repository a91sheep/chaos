package com.store59.printapi.controller.order;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.order.CancelMyOrderParam;
import com.store59.printapi.model.param.order.MyOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.order.MyOrderResult;
import com.store59.printapi.model.result.order.MyOrderResultList;
import com.store59.printapi.service.order.MyOrderService;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 2016/01/13
 * @since 1.0
 */
@RestController
@RequestMapping("/order/*")
public class MyOrderController {

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    @Autowired
    private MyOrderService myOrderService;

    /**
     * 获取我的订单
     *
     * @param myOrderParam
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object getMyOrderList(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                 @Valid MyOrderParam myOrderParam, BindingResult result) {
        Datagram<MyOrderResultList> datagram = new Datagram<>();
        if (result.hasErrors()) {
            datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
            datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
            datagram.setIsApp(false);
            return datagram;
        }

        String loginId = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        // Login id信息取得
        LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginId, LoginIdInfo.class);

        List<MyOrderResult> list = myOrderService.getMyOrderList(loginIdInfo.getUid(), myOrderParam);

        MyOrderResultList orderList = new MyOrderResultList();
        orderList.setMyOrderResultList(list);
        if (list.size() > 0 && myOrderParam.getLimit() != null) {
            if (list.size() == Integer.valueOf(myOrderParam.getLimit())) {
                orderList.setFlag(true);
            } else {
                orderList.setFlag(false);
            }
        } else {
            orderList.setFlag(false);
        }

        // 返回值映射
        datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
        datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
        datagram.setData(orderList);
        datagram.setIsApp(false);
        return datagram;
    }

    /**
     * 取消订单
     *
     * @param tokenId
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Object cancelOrderById(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                  @Valid CancelMyOrderParam param, BindingResult result) {
        if (result.hasErrors()) {
            return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage());
        }
        String loginId = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        // Login id信息取得
        LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginId, LoginIdInfo.class);

        Boolean flag = myOrderService.cancelOrderById(loginIdInfo.getUid(), param.getOrderId());
        // 返回值映射
        return DatagramHelper.getDatagram(flag, CommonConstant.GLOBAL_STATUS_SUCCESS,
                CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
    }

    /**
     * 获取我的订单
     *
     * @param myOrderParam
     * @return
     */
    @RequestMapping(value = "/mylist", method = RequestMethod.GET)
    public Object myOrderList(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId, @RequestParam(required=false)String uid,
                              @Valid MyOrderParam myOrderParam, BindingResult result) {
        Datagram<MyOrderResultList> datagram = new Datagram<>();
        if (result.hasErrors()) {
            datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
            datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
            datagram.setIsApp(false);
            return datagram;
        }

        String loginId = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        // Login id信息取得
        LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginId, LoginIdInfo.class);

        MyOrderResultList orderList = myOrderService.getOrderList(uid==null?loginIdInfo.getUid():Long.parseLong(uid), myOrderParam);

        // 返回值映射
        datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
        datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
        datagram.setData(orderList);
        datagram.setIsApp(false);
        return datagram;
    }

    /**
     * PC根据订单号获取订单详情
     *
     * @param tokenId
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object getMyOrderDetailList(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                       @RequestParam(required = true) String orderSn) {
        String loginId = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginId, LoginIdInfo.class);
        // 根据订单号获取订单详情
        MyOrderResult orderInfo = myOrderService.myOrderDetail(orderSn, loginIdInfo.getUid());
        return DatagramHelper.getDatagram(orderInfo, 0, "", true);
    }
}
