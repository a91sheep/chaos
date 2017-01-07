package com.store59.printapi.controller.wechat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.wechat.SignatureUtil;
import com.store59.printapi.common.wechat.XMLConverUtil;
import com.store59.printapi.common.wechat.api.TokenAPI;
import com.store59.printapi.model.param.wechat.message.EventMessage;
import com.store59.printapi.model.param.wechat.token.Token;
import com.store59.printapi.service.wechat.WeChatDispacherService;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月10日 下午5:59:34 微信用户验证测试类
 */
@RestController
@RequestMapping("wechat")
public class WechatController {
	@Autowired
	WeChatDispacherService dispacherService;
	private static final String token = "2473221efb4b4a0e95e1501e9f78a8ee";
	private Logger logger = Logger.getLogger(WechatController.class);

	@RequestMapping("info")
	public void validate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletInputStream inputStream = request.getInputStream();
		ServletOutputStream outputStream = response.getOutputStream();
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		// logger.info("接入参数signature："+signature);
		// logger.info("接入参数timestamp："+timestamp);
		// logger.info("接入参数nonce："+nonce);
		// logger.info("接入参数echostr："+echostr);
		// 首次请求申请验证,返回echostr
		if (echostr != null) {
			// 验证请求签名
			if (!signature.equals(SignatureUtil.generateEventMessageSignature(token, timestamp, nonce))) {
				logger.info("请求签名验证不通过timestamp:" + timestamp + ",nonce:" + nonce + ",signature:" + signature);
				return;
			}
			logger.info("首次接入返回：" + echostr);
			outputStreamWrite(outputStream, echostr);
			return;
		}
		// 验证请求签名
		if (!signature.equals(SignatureUtil.generateEventMessageSignature(token, timestamp, nonce))) {
			logger.info("请求签名验证不通过timestamp:" + timestamp + ",nonce:" + nonce + ",signature:" + signature);
			return;
		}
		if (inputStream != null) {
			// 转换XML
			EventMessage eventMessage = XMLConverUtil.convertToObject(EventMessage.class, inputStream);
			String rs = dispacherService.msgDispacher(eventMessage);
			outputStreamWrite(outputStream, rs);
			return;
		}
	}

	/**
	 * 数据流输出
	 * 
	 * @param outputStream
	 * @param text
	 * @return
	 */
	private boolean outputStreamWrite(OutputStream outputStream, String text) {
		logger.info("被动响应数据：" + text);
		try {
			outputStream.write(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${print.wechat.appid}")
	private String appid;
	@Value("${print.wechat.secret}")
	private String secret;

	@RequestMapping("token")
	public String refurbish(@RequestParam(required=false)String appId,@RequestParam(required=false)String appSecret) {
		if (valueOpsCache.setIfAbsent(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX,
				String.valueOf(System.currentTimeMillis()))) {
			Token token = null;
			if(appId!=null&&appSecret!=null){
				token = TokenAPI.token(appId, appSecret);
			}else{
				token = TokenAPI.token(appid, secret);
			}

			if (token.getAccess_token() != null){
				if(appId!=null&&appSecret!=null){
					valueOpsCache.set(CommonConstant.WECHAT_TOKEN_PREFIX + appId, token.getAccess_token(), 7200,
						TimeUnit.SECONDS);
					valueOpsCache.set(CommonConstant.WECHAT_TOKEN_PREFIX, token.getAccess_token(), 7200,
							TimeUnit.SECONDS);
					logger.info(
							"ACCESS_TOKEN refurbish by someone with appid:{}" + appId + ";token:" + token.getAccess_token());
				}else{
					valueOpsCache.set(CommonConstant.WECHAT_TOKEN_PREFIX + appid, token.getAccess_token(), 7200,
							TimeUnit.SECONDS);
					logger.info(
							"ACCESS_TOKEN refurbish by someone with appid:{}" + appid + ";token:" + token.getAccess_token());
				}
				
			}
			valueOpsCache.getOperations().delete(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX);
		}
		return "ok";
	}

}
