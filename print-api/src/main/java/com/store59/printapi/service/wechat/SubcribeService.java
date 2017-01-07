package com.store59.printapi.service.wechat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.store59.ad.common.api.UserWeChatService;
import com.store59.ad.common.data.AdConst;
import com.store59.ad.common.filter.UserWeChatFilter;
import com.store59.ad.common.model.UserWeChat;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.XMLConverUtil;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.wechat.message.EventMessage;
import com.store59.printapi.model.param.wechat.message.TextMessage;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月11日 下午4:25:44 微信关注service
 */
@Service
public class SubcribeService {

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

	Logger logger = Logger.getLogger(SubcribeService.class);

	public void sbucribe(EventMessage msg) {
		logger.info("获取的事件消息内容：" + XMLConverUtil.convertToXML(msg));
		// if (!isBinding(msg.getFromUserName())) {
		TextMessage textMsg = new TextMessage(msg.getFromUserName(), "欢迎关注59云印。<a href=\""
				+ callbackWechatUrl + "?openid=" + msg.getFromUserName() + "\">点击这里登录或注册59账号</a>，绑定后即可打印照片");
		logger.info("用户未绑定" + msg.getFromUserName() + ";推送内容" + JsonUtil.getJsonFromObject(textMsg));
		logger.info("获取默认token：" + tokenManager.getToken(appid));
		MessageAPI.messageCustomSend(tokenManager.getToken(appid), textMsg);
		return;
		// }
	}

	private boolean isBinding(String openid) {
		String cashUid = valueOpsCache.get(CommonConstant.WECHAT_OPENID_UID_PREFIX + openid);
		if (!StringUtil.isBlank(cashUid)) {
			return true;
		}
		// 查询用户是否绑定
		UserWeChatFilter filter = new UserWeChatFilter();
		filter.setSource(AdConst.WEIXIN_SOURCE_PRINT);
		filter.setOpenId(openid);
		Result<List<UserWeChat>> list = userWeCahtService.findByFilter(filter);
		logger.info("查询绑定结果：" + JsonUtil.getJsonFromObject(list));
		String uid = "";
		if (list.getStatus() == 0 && list.getData().size() > 0) {
			uid = list.getData().get(0).getUid().toString();
		} else {
			return false;
		}
		// 如果绑定则添加uid和openid映射缓存
		valueOpsCache.set(CommonConstant.WECHAT_UID_OPENID_PREFIX + uid, openid, cookieMaxAge + 10, TimeUnit.SECONDS);
		valueOpsCache.set(CommonConstant.WECHAT_OPENID_UID_PREFIX + openid, uid, cookieMaxAge + 10, TimeUnit.SECONDS);
		return true;
	}
}
