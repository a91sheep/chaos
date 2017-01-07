package com.store59.printapi.controller.gala;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.store59.base.common.api.SmsAuthApi;
import com.store59.kylin.common.model.Result;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.model.param.PhoneParam;
import com.store59.printapi.model.param.SmsPhoneParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.service.gala.PrintGalaService;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月11日 上午11:44:38 520活动
 */
@RestController
@RequestMapping("/gala520/*")
public class PrintGala520Controller {
	@Autowired
	private SmsAuthApi smsAuthApi;
	@Autowired
	private PrintGalaService printGalaService;

//	private Logger logger = org.slf4j.LoggerFactory.getLogger(PrintGala520Controller.class);

	/**
	 * 验证验证码，成功发照片优惠券
	 * @param response
	 * @param param
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public Object smsValidate(HttpServletResponse response, @Valid SmsPhoneParam param, BindingResult result)
			throws Exception {
		Datagram<Integer> datagram = new Datagram<>();
		if (result.hasErrors()) {
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
			datagram.setIsApp(false);
			return datagram;
		}

		Result<Boolean> codeAuth = smsAuthApi.checkAuthCode("phone_gala520", param.getPhone(), param.getCode());
		if (codeAuth == null || codeAuth.getStatus() != 0 || codeAuth.getData() == null || !codeAuth.getData()) {
			throw new CommonException("短信验证失败");
		}
		// 添加优惠券信息，开始发放优惠券
		printGalaService.sendCoupon(param.getPhone());
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		return datagram;
	}

	/**
	 * 发送手机验证码
	 * @param param
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
	@ResponseBody
	public Object sendSms(@Valid PhoneParam param, BindingResult result) {
		Datagram<String> datagram = new Datagram<>();
		if (result.hasErrors()) {
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(result.getAllErrors().get(0).getDefaultMessage());
			datagram.setIsApp(false);
			return datagram;
		}
		Result<String> code = smsAuthApi.sendAuthCode("phone_gala520", param.getPhone(), null);
		if (code == null || code.getStatus() != 0 || code.getData() == null) {
			throw new CommonException("短信发送失败");
		}
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		return datagram;
	}

}
