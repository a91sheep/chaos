package com.store59.printapi.controller.coupon;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.Constant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.controller.BaseController;
import com.store59.printapi.model.param.coupon.AppCouponListParam;
import com.store59.printapi.model.param.coupon.CouponListParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderParam;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.app.TokenInfo;
import com.store59.printapi.service.coupon.CouponLocalService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/11
 * @since 1.0
 */
@RestController
@RequestMapping("/print/*")
public class CouponController extends BaseController {
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Autowired
	private CouponLocalService couponLocalService;

	// PC
	@RequestMapping(value = "/coupon/list", method = RequestMethod.GET)
	public Object getCouponListByUid(HttpServletRequest request, HttpServletResponse response,
			@Valid CouponListParam param, BindingResult result) throws IOException {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage());
		}
		Long uid = param.getUid();// H5访问
		if (uid == null || uid == 0) {
			if (StringUtil.isBlank(param.getToken())) {// PC访问
				Cookie[] cookies = request.getCookies();
				String tokenId = null;
				for (Cookie coo : cookies) {
					if (coo.getName().equals(CommonConstant.KEY_COOKIE_NAME_TOKEN)) {
						tokenId = coo.getValue();
					}
				}
				if ("no".equals(param.getIsAll()) && null == param.getAmount()) {
					return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID, "文档金额不能为空!");
				}

				String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
				LoginIdInfo loginIdInfo = new LoginIdInfo();
				try {
					loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
				} catch (Exception e) {
					throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
				}
				uid = loginIdInfo.getUid();
			} else {// APP访问
				TokenInfo tokenInfo = JsonUtil.getObjectFromJson(
						valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
				if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
					throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
				}
				uid = tokenInfo.getUid();
			}
		}
		return couponLocalService.getCouponListByUid(uid, param.getIsAll(), param.getAmount(), param.getType());
	}

	// APP 创建购物车接口
	@RequestMapping(value = "/cart/create", method = RequestMethod.POST)
	public Object getUsefulCoupon(HttpServletRequest request, HttpServletResponse response,
			@Valid AppCreateOrderParam param, BindingResult result) throws IOException {
		Long uid = new Long(0); // 未登录,uid标示为0
		if (!StringUtil.isBlank(valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()))) {
			TokenInfo tokenInfo = JsonUtil.getObjectFromJson(
					valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
			uid = tokenInfo.getUid();
		}

		return couponLocalService.getUsefulCoupon(param, uid);
	}

	// APP coupon列表
	@RequestMapping(value = "/couponpic/list", method = RequestMethod.GET)
	public Object getCouponListByUid(@Valid AppCouponListParam param, BindingResult result) throws AppException {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage());
		}
		Long uid = param.getUid();
		if (uid == null || uid == 0) {
			TokenInfo tokenInfo = JsonUtil.getObjectFromJson(
					valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
			if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
				throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
			}
			uid = tokenInfo.getUid();
		}
		return couponLocalService.getAppCouponListByUid(uid, param.getIsAll(), param.getAmount(), param.getType());
	}

}
