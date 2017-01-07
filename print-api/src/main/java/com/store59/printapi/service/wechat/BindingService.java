package com.store59.printapi.service.wechat;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.ad.common.api.UserWeChatService;
import com.store59.ad.common.data.AdConst;
import com.store59.ad.common.filter.UserWeChatFilter;
import com.store59.ad.common.model.UserWeChat;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.utils.TypeConverter;
import com.store59.printapi.common.utils.UrlUtil;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.api.UserAPI;
import com.store59.printapi.model.param.wechat.user.WeChatUser;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.LoginInfo;
import com.store59.printapi.model.result.UserInfo;
import com.store59.user.common.api.UserApi;
import com.store59.user.common.model.User;

//@Service
public class BindingService {
	@Autowired
	private HttpClientRequest httpClientRequest;
	@Value("${account.center.internal}")
	private String accountInternalBaseUrl;
	@Value("${account.center.key}")
	private String accountKey;
	@Value("${print.wechat.appid}")
	private String appid;
	@Autowired
	private UserApi userApi;

	@Autowired
	UserWeChatService userWeCahtService;
	@Autowired
	TokenManager tokenManager;
	
    private Logger logger = LoggerFactory.getLogger(BindingService.class);
    
	 public LoginInfo doLogin(String smartTicket,String openid)
	            throws Exception {
	        LoginInfo loginInfo = new LoginInfo();
	        // 进行signin check
	        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
	        params.add(new BasicNameValuePair("smartTicket", smartTicket));
	        params.add(new BasicNameValuePair("token", accountKey));
	        String ret = "";
	        try {
	        	logger.info("用户中心回调地址："+accountInternalBaseUrl);
	            ret = httpClientRequest.conn(UrlUtil.getLoginCheckUrl(accountInternalBaseUrl, CommonConstant.PATH_ACCOUNT_CENTER_SIGNIN_CHECK),
	                    CommonConstant.HTTP_METHOD_POST,
	                    params);
	        } catch (Exception e) {
	        	logger.error("用户中心回调获取用户信息出错："+e.getMessage()+e.getStackTrace());
	            throw new Exception(e);
	        }
	        Datagram<Map<String, Object>> datagramWork = JsonUtil.getObjectFromJson(ret, new TypeReference<Datagram<Map<String, Object>>>() {
	        });

	        // signin检查错误，返回相应错误信息（跳转至错误信息页面）
	        if (datagramWork.getCode() != 0) {
	            loginInfo.setErrMsg(MessageConstant.LOGIN_SIGNIN_INVALID);
	            return loginInfo;
	        }

	        // uid取得
	        Object uidWork = datagramWork.getData().get("id");
	        logger.info("用户中心回调数据："+JsonUtil.getJsonFromObject(datagramWork));
	        long uid = TypeConverter.str2Long(String.valueOf(uidWork));
	        logger.info("登录用户的uid:" + uid);

	        //获取用户信息
	        Result<User> userResult = userApi.getUser(uid);
	        logger.info("获取用户数据结果："+JsonUtil.getJsonFromObject(userResult));
	        if (userResult == null || userResult.getStatus() != 0 || userResult.getData() == null) {
	            loginInfo.setErrMsg(MessageConstant.USER_NORECORD);
	            return loginInfo;
	        }
	        LoginIdInfo loginIdInfo = new LoginIdInfo();
	        loginIdInfo.setUid(uid);

	        User user = userResult.getData();

	        UserInfo userInfo = new UserInfo();
	        userInfo.setUid(user.getUid());
	        userInfo.setUname(user.getUname());
	        userInfo.setPhone(user.getPhone());

	        loginInfo.setUserInfo(userInfo);
	        loginInfo.setLoginIdInfo(loginIdInfo);
	        UserWeChatFilter uwchatFilter=new UserWeChatFilter();
//	        uwchatFilter.setOpenId(openid);
	        uwchatFilter.setSource(AdConst.WEIXIN_SOURCE_PRINT);
	        uwchatFilter.setUid(uid);
	        Result<List<UserWeChat>> wechatResult=userWeCahtService.findByFilter(uwchatFilter);
	        uwchatFilter.setOpenId(openid);
	        uwchatFilter.setUid(null);
	        Result<List<UserWeChat>> wechatResultOpenid=userWeCahtService.findByFilter(uwchatFilter);
	        //添加用户绑定信息1.更新unionid，2添加绑定数据
	        if(wechatResult.getStatus()==0&&wechatResult.getData()!=null&&wechatResult.getData().size()==0
	        		&&wechatResultOpenid.getStatus()==0&&wechatResultOpenid.getData()!=null&&wechatResultOpenid.getData().size()==0){
	        	logger.info("token:"+tokenManager.getToken(appid));
	        	WeChatUser wcUser=UserAPI.userInfo(tokenManager.getToken(appid), openid);
	        	String unionId=wcUser.getUnionid();
	        	User usermsg=new User();
	        	usermsg.setUid(uid);
	        	usermsg.setThirdWeixin(unionId);
	        	if(!StringUtil.isBlank(unionId)){
	        		Result<Boolean> uResult=userApi.updateUser(usermsg);
	        		logger.info("更新用户信息结果："+JsonUtil.getJsonFromObject(uResult));
	        	UserWeChat uwchat=new UserWeChat();
	        	uwchat.setOpenId(openid);
	        	uwchat.setSource(AdConst.WEIXIN_SOURCE_PRINT);
	        	uwchat.setUid(uid);
	        	uwchat.setUnionId(unionId);
	        	uwchat.setUpdateTime(System.currentTimeMillis()/1000);
	        	Result<UserWeChat> weResult=userWeCahtService.insert(uwchat);
	        	logger.info("添加微信关系信息结果："+JsonUtil.getJsonFromObject(weResult));
	        	}else{
	        		logger.info("获取unionId失败拒绝添加映射关系");
	        	}
	        }else{
	        	logger.info("用户已经绑定过了openid-"+openid+";uid-"+uid);
	        	loginInfo.setErrMsg(MessageConstant.WECHAT_HAS_BINDED);
	        }
	        return loginInfo;
	    }

}
