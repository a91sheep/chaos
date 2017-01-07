package com.store59.printapi.service.wechat;

import javax.annotation.Resource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.store59.ad.common.api.UserWeChatService;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.XMLConverUtil;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.wechat.message.EventMessage;
import com.store59.printapi.model.param.wechat.message.TextMessage;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月11日 下午4:25:44
 * 微信文字回复消息
 */
@Service
public class TextService {

	@Autowired
	UserWeChatService userWeCahtService;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;
	@Value("${print.wechat.callback.baseurl}")
	private String callbackWechatUrl;
	@Value("${print.wechat.appid}")
	private String appid;
	
	@Autowired
	TokenManager tokenManager;
	
	Logger logger = Logger.getLogger(TextService.class);

	public void textCheck(EventMessage msg) {
		logger.info("获取的文本消息内容：" + XMLConverUtil.convertToXML(msg));
//		if(msg.getContent().trim().equals("178")){
//			TextMessage textMsg = new TextMessage(msg.getFromUserName(), " ☞ <a href=\"http://e.gfd178.com/ZD0H6Uni\">点我免费打照片</a> ☜");
//			logger.info("178获取默认token：" + tokenManager.getToken(appid));
//			MessageAPI.messageCustomSend(tokenManager.getToken(appid), textMsg);
//			return;
//		}
	}

}
