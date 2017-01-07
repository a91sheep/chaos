package com.store59.printapi.service.wechat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.ad.common.api.UserWeChatService;
import com.store59.ad.common.data.AdConst;
import com.store59.ad.common.filter.UserWeChatFilter;
import com.store59.ad.common.model.UserWeChat;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.XMLConverUtil;
import com.store59.printapi.common.wechat.api.MediaAPI;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.wechat.message.EventMessage;
import com.store59.printapi.model.param.wechat.message.TextMessage;
import com.store59.printapi.model.param.wechat.pic.PicWechat;

/**
 * 
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月11日 下午4:25:44 微信图片上传service
 */
@Service
public class PicService {

	@Autowired
	UserWeChatService userWeCahtService;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;
	@Value("${print.wechat.callback.baseurl}")
	private String callbackWechatUrl;
	@Value("${aliyun.oss.print.bucketName}")
	private String bucketName;
	@Value("${aliyun.oss.print.domainName}")
	private String domainName;
	@Value("${print.wechat.appid}")
	private String appid;
	@Autowired
	private OSSClient ossClient;

	@Autowired
	SendMsgService sendMsgService;
	@Autowired
	TokenManager tokenManager;
	Logger logger = Logger.getLogger(PicService.class);

	public void picCheck(EventMessage msg) {
		logger.info("获取的消息内容：" + XMLConverUtil.convertToXML(msg));
		if (!isBinding(msg.getFromUserName())) {
			TextMessage textMsg = new TextMessage(msg.getFromUserName(), "绑定账号才可以打印照片。<a href=\"" + callbackWechatUrl
					+ "?openid=" + msg.getFromUserName() + "\">点击这里绑定59帐号</a>");
			logger.info("用户未绑定" + msg.getFromUserName() + ";推送内容" + JsonUtil.getJsonFromObject(textMsg));
			logger.info("获取默认token：" + tokenManager.getToken(appid));
			MessageAPI.messageCustomSend(tokenManager.getToken(appid), textMsg);
			return;
		}
		new Thread() {
			public void run() {
				try {
					logger.info("启动处理线程");
					String openid = msg.getFromUserName();
					byte[] file=MediaAPI.mediaGet(tokenManager.getToken(appid), msg.getMediaId());
					BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(file);
			        String conver_key = FileUtil.upload(multipartFile, "image/jpeg", "image.jpeg", ossClient, bucketName);
			        String doc_path = domainName + "/" + conver_key;
					logger.info("上传阿里云返回图片地址："+doc_path);
					msg.setPicUrl(doc_path);
					PicWechat pic = new PicWechat();
					String picStr = valueOpsCache.get(CommonConstant.WECHAT_OPENID_PREFIX + openid);
					// logger.info("获取存储信息："+picStr);
					if (!StringUtil.isBlank(picStr)) {
						pic = JsonUtil.getObjectFromJson(picStr, new TypeReference<PicWechat>() {
						});
					}
					pic.setTimestamp(msg.getCreateTime());
					List<EventMessage> list = pic.getList() == null ? new LinkedList<EventMessage>() : pic.getList();
					list.add(msg);
					pic.setList(list);
					valueOpsCache.set(CommonConstant.WECHAT_OPENID_PREFIX + openid, JsonUtil.getJsonFromObject(pic),
							cookieMaxAge + 10, TimeUnit.SECONDS);
					sendMsgService.addList(openid);
				} catch (Exception e) {
					logger.info("处理图片消息线程出现问题：" + e.getMessage() + e.getStackTrace());
				}
			}
		}.start();
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
