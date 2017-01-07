package com.yunyin.print.main.api.controller.pc;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.yunyin.base.common.model.User;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.main.api.common.constant.CommonConstant;
import com.yunyin.print.main.api.model.param.pc.CreateOrderParam;
import com.yunyin.print.main.api.model.param.pc.OrderCancelParam;
import com.yunyin.print.main.api.model.param.pc.OrderListParam;
import com.yunyin.print.main.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
@RestController
@RequestMapping("/print/web/order/*")
public class OrderController {
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Autowired
    private OrderService                    orderService;

    /**
     * 获取订单
     *
     * @param tokenId
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public Result<PrintOrder> getOrderByOrderId(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                                @RequestParam(required = true) String orderId) {

        String userJson = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        User user = JsonUtil.getObjectFromJson(userJson, User.class);
        if (user == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前用户未登录");
        }
        return orderService.getOrderByOrderId(orderId, user.getUid());
    }

    /**
     * 获取订单列表
     *
     * @param tokenId
     * @param param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<PrintOrder>> getMyOrderList(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                                   OrderListParam param) {

        String userJson = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        User user = JsonUtil.getObjectFromJson(userJson, User.class);
        if (user == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前用户未登录");
        }
        return orderService.getMyOrderList(param, user.getUid());
    }

    /**
     * 取消订单
     *
     * @param tokenId
     * @param param
     * @param result
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Object cancelOrderById(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
                                  @Valid OrderCancelParam param, BindingResult result) {
        if (result.hasErrors()) {
            return ResultHelper.genResult(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage());
        }
        String userJson = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        User user = JsonUtil.getObjectFromJson(userJson, User.class);
        if (user == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前用户未登录");
        }
        return orderService.cancelOrder(user.getUid(), param.getOrderId());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object createOrder(@Valid CreateOrderParam param, BindingResult result, @CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId) {
        if (result.hasErrors()) {
            return ResultHelper.genResult(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage());
        }
        String userJson = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        User user = JsonUtil.getObjectFromJson(userJson, User.class);
        if (user == null) {
            throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "当前用户未登录");
        }
        return orderService.createOrder(param, user);
    }
}
