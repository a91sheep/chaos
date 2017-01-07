package com.store59.printapi.controller.CreateOrder;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.ad.AdFreeParam;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.service.createOrder.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/4/20
 * @since 1.0
 */
@RestController
@RequestMapping("/ad/*")
public class AdInfoController {
    @Autowired
    private AdService adService;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object createOrder(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,@Valid AdFreeParam param, BindingResult result) {
        if (result.hasErrors()) {
            return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage());
        }
        String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        LoginIdInfo loginIdInfo = new LoginIdInfo();
        try {
            loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
        } catch (Exception e) {
            throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
        }
        Long uid = loginIdInfo.getUid();
        return adService.getAdFreeAmount(param,uid);
    }
}
