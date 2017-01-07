package com.store59.printapi.controller.CreateOrder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.SignatureUtils;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.Constant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.param.createOrder.PicCreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.UrlResult;
import com.store59.printapi.model.result.app.TokenInfo;
import com.store59.printapi.service.createOrder.CreateOrderService;
import com.store59.printapi.service.createOrder.PicCreateOrderService;

@RestController
@RequestMapping("print")
public class PicOrderCreateController {
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;
	@Autowired
	private PicCreateOrderService picOrderService;
	@Autowired
	private CreateOrderService createOrderService;
	
	@Value("${zhifu.key}")
	private String zhifuKey;

	Logger logger = Logger.getLogger(PicOrderCreateController.class);

	/**
	 * H5 图片列表
	 * 
	 * @param uid
	 * @param shop_id
	 * @return
	 */
	@RequestMapping(value = "cartpic/list")
	public Object getCartList(@RequestParam(required = true) String uid,
			@RequestParam(required = true) String shop_id) {
		return picOrderService.getCartList(uid, shop_id);
	}

//	// H5 创建购物车接口
//	@RequestMapping(value = "/cartpic/create", method = RequestMethod.POST)
//	public Object createCart(@Valid PicCreateOrderParam param, BindingResult result) throws IOException {
//		if (result.hasErrors()) {
//			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
//					result.getAllErrors().get(0).getDefaultMessage());
//		}
//		Long uid = 0L;
//		if (!StringUtil.isBlank(valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()))) {
//			TokenInfo tokenInfo = JsonUtil.getObjectFromJson(
//					valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
//			uid = tokenInfo.getUid();
//		}
//		return picOrderService.cart(param, uid);
//	}

	/**
	 * 照片打印创建订单
	 * 
	 * @param param
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orderpic/create", method = RequestMethod.POST)
	public Object createOrder(@Valid PicCreateOrderParam param, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage(), true);
		}
		String uid = param.getUid();
		if (StringUtil.isBlank(uid)) {
			if (StringUtil.isBlank(param.getToken())) {
				Cookie[] cookies = request.getCookies();
				String tokenId = null;
				for (Cookie coo : cookies) {
					if (coo.getName().equals(CommonConstant.KEY_COOKIE_NAME_TOKEN)) {
						tokenId = coo.getValue();
					}
				}
				String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
				LoginIdInfo loginIdInfo = new LoginIdInfo();
				try {
					loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
				} catch (Exception e) {
					throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
				}
				uid = String.valueOf(loginIdInfo.getUid());
			} else {
				TokenInfo tokenInfo = JsonUtil.getObjectFromJson(
						valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
				if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
					throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
				}
				param.setDocType(param.getDoc_type());
				uid = String.valueOf(tokenInfo.getUid());
			}
		}
		return picOrderService.createOrder(param, Long.parseLong(uid));
	}

	@RequestMapping(value = "/fileuploadpic", method = RequestMethod.POST)
	public Datagram<UrlResult> fileUpLoad(@RequestParam(required = true) String file,
			@RequestParam(required = true) String length, @RequestParam(required = true) String sourFileName,
			@RequestParam(required = true) String ext) {
		Datagram<UrlResult> result = null;
		if (file.length() == Integer.parseInt(length)) {
			try {
				result = picOrderService.fileupload(file, sourFileName, ext);
			} catch (Exception e) {
				logger.error("截图上传出错" + e.getMessage() + e.getStackTrace());
			}
		} else {
			throw new BaseException(CommonConstant.STATUS_REQUEST_FILE_INVALID,
					CommonConstant.MSG_STATUS_MAXFILEUPLOAD);
		}
		return result;
	}

	@RequestMapping(value = "/signpic", method = RequestMethod.GET)
	public Object paymentAlipay(@RequestParam(required = true) String orderId,
			@RequestParam(required = true) String openId, @RequestParam(required = true) String foodName,
			@RequestParam(required = true) String money, @RequestParam(required = false) String notifyUrl,
			@RequestParam(required = true) String returnUrl) {
		logger.debug("接收到得请求参数 orderId：{}" + orderId);

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderId", orderId);
		paramsMap.put("openId", openId);
		paramsMap.put("foodName", "照片打印");
		paramsMap.put("money", money);
//		paramsMap.put("notifyUrl", notifyUrl);
		paramsMap.put("returnUrl", returnUrl);
		logger.info("加密参数：" + JsonUtil.getJsonFromObject(paramsMap).toString());
		String sign = SignatureUtils.sign(paramsMap, zhifuKey);
		logger.info("生成sign" + sign);
		paramsMap.put("sign", sign);
		return DatagramHelper.getDatagram(paramsMap, CommonConstant.GLOBAL_STATUS_SUCCESS,
				CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	@RequestMapping("/test")
	public Object test() {
		Thread a = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "start");
				if (valueOpsCache.setIfAbsent(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX,
						String.valueOf(System.currentTimeMillis()))) {
					System.out.println("1:" + valueOpsCache.get(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX));
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						valueOpsCache.getOperations().delete(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX);
					}
				} else {
					System.out.println("2:" + valueOpsCache.get(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX));
				}
				System.out.println(Thread.currentThread().getName() + "over");
			}
		};
		Thread b = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "start");
				if (valueOpsCache.setIfAbsent(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX,
						String.valueOf(System.currentTimeMillis()))) {
					System.out.println("1:" + valueOpsCache.get(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX));
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						valueOpsCache.getOperations().delete(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX);
					}
				} else {
					System.out.println("2:" + valueOpsCache.get(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX));
				}
				System.out.println(Thread.currentThread().getName() + "over");
			}
		};
		a.start();
		b.start();
		return "0";
	}

	@RequestMapping(value = "pic/conversion", method = RequestMethod.POST)
	public Object fileParams(@RequestParam(required = true) String url,
			@RequestParam(required = true) String file_name) throws Exception {
		if (url.endsWith(".doc") || url.endsWith(".docx") || url.endsWith(".ppt") || url.endsWith(".pptx")
				|| url.endsWith(".pdf") || url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".jpeg")) {
			logger.info("上传文件名称:" + url + ",文件格式符合要求");
		} else {
			logger.info("上传文件名:" + url + ",文件格式非法!");
			throw new AppException(CommonConstant.STATUS_REQUEST_FILE_INVALID, "上传文件名:" + url + ",文件格式非法!");
		}
		return createOrderService.fileParams(url, file_name, true);
	}
}
