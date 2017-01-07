package com.store59.printapi.controller.order;

import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.Constant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.controller.BaseController;
import com.store59.printapi.model.param.order.MyOrderParam;
import com.store59.printapi.model.result.app.TokenInfo;
import com.store59.printapi.model.result.order.AppOrder;
import com.store59.printapi.model.result.order.AppOrderList;
import com.store59.printapi.service.order.MyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
@RestController
@RequestMapping("/print/order/*")
public class AppOrderController extends BaseController {
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
    public Object getMyOrderList(MyOrderParam myOrderParam, BindingResult result) {
        Long uid = null;
        if (StringUtil.isBlank(myOrderParam.getUserid())) {
            String info = valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + myOrderParam.getToken());
            if (StringUtil.isBlank(info)) {
                throw new AppException(Constant.STATUS_TOKEN_INVALID, Constant.MSG_TOKEN_INVALID);
            }

            TokenInfo tokenInfo = JsonUtil.getObjectFromJson(info, TokenInfo.class);
            if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
                throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
            }
            uid = tokenInfo.getUid();
        } else {
            uid = Long.parseLong(myOrderParam.getUserid());
        }
        List<AppOrder> list = myOrderService.getOrderByUidAndStatus(uid, myOrderParam);

        AppOrderList orderList = new AppOrderList();
        orderList.setOrders(list);

        return DatagramHelper.getDatagram(orderList, 0, "", true);
    }

    /**
     * 获取我的订单详情
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object getMyOrderDetailList(String token, String order_sn, String uid) {
        if (StringUtil.isBlank(order_sn)) {
            throw new AppException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "订单号不能为空!");
        }
        if (StringUtil.isBlank(uid)) {
            String info = valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + token);
            if (StringUtil.isBlank(info)) {
                throw new AppException(Constant.STATUS_TOKEN_INVALID, Constant.MSG_TOKEN_INVALID);
            }

            TokenInfo tokenInfo = JsonUtil.getObjectFromJson(info, TokenInfo.class);
            if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
                throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
            }
            uid = String.valueOf(tokenInfo.getUid());
        }

        // 根据订单号获取订单详情
        AppOrder appOrder = myOrderService.getOrderDetailsByOrderId(order_sn, Long.parseLong(uid));

        return DatagramHelper.getDatagram(appOrder, 0, "", true);
    }

    /**
     * 取消订单
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Object cancelOrderById(String token, String order_sn, String uid) {
        if (StringUtil.isBlank(order_sn)) {
            throw new AppException(CommonConstant.STATUS_REQUEST_DATA_INVALID, "订单号不能为空!");
        }
        if (StringUtil.isBlank(uid)) {
            TokenInfo tokenInfo = JsonUtil.getObjectFromJson(valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + token),
                    TokenInfo.class);
            if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
                throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
            }
            uid = String.valueOf(tokenInfo.getUid());
        }

        Boolean flag = myOrderService.cancelOrderById_APP(Long.parseLong(uid), order_sn);
        // 返回值映射
        return DatagramHelper.getDatagram(flag, CommonConstant.GLOBAL_STATUS_SUCCESS,
                CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
    }
}