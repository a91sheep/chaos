/**
 *
 */
package com.store59.printapi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.event.push.common.enums.PushTargetEnum;
import com.store59.event.push.common.helper.PushEntityHelper;
import com.store59.event.push.common.model.PushContent;
import com.store59.event.push.common.model.PushEntity;
import com.store59.event.push.common.rabbit.RabbitSender;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.param.RangeParams;
import com.store59.printapi.model.param.common.DormShopParam;
import com.store59.printapi.model.param.common.DormShopPriceParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.SelectBox;
import com.store59.printapi.model.result.UserInfo;
import com.store59.printapi.model.result.common.DormentryInfo;
import com.store59.printapi.model.result.common.SchoolInfo;
import com.store59.printapi.model.result.common.ShopInfos;
import com.store59.printapi.model.result.common.ShopMsg;
import com.store59.printapi.model.result.common.SiteInfo;
import com.store59.printapi.service.CommonService;
import com.store59.user.common.api.UserApi;
import com.store59.user.common.filter.UserFilter;
import com.store59.user.common.model.User;

/**
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月13日
 * @since 1.1
 */
@RestController
@RequestMapping("/common/*")
public class CommonController {
	@Autowired
	private CommonService commonService;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;

	/**
	 * 获取省列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/provinceList", method = RequestMethod.GET)
	public Object getProvinceList() {
		String provinceJson = valueOpsCache.get(CommonConstant.KEY_REDIS_PROVINCE_PREFIX + "json");
		if (StringUtil.isBlank(provinceJson)) {
			Datagram<SelectBox> datagram = commonService.getProvinceList();
			valueOpsCache.set(CommonConstant.KEY_REDIS_PROVINCE_PREFIX + "json", JsonUtil.getJsonFromObject(datagram),
					cookieMaxAge + 10, TimeUnit.SECONDS);
			return datagram;
		}
		return JsonUtil.getObjectFromJson(provinceJson, new TypeReference<Datagram<SelectBox>>() {
		});
	}

	/**
	 * 获取市列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/cityList", method = RequestMethod.GET)
	public Object getCityList(RangeParams params, BindingResult result) {
		if (StringUtil.isBlank(params.getProvinceId())) {
			Datagram datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(MessageConstant.Province_Id);
			datagram.setIsApp(false);
			return datagram;
		}
		String cityJson = valueOpsCache.get(CommonConstant.KEY_REDIS_CITY_PREFIX + params.getProvinceId());
		if (StringUtil.isBlank(cityJson)) {
			Datagram<SelectBox> datagram = commonService.getCityList(Integer.valueOf(params.getProvinceId()));
			valueOpsCache.set(CommonConstant.KEY_REDIS_CITY_PREFIX + params.getProvinceId(),
					JsonUtil.getJsonFromObject(datagram), cookieMaxAge + 10, TimeUnit.SECONDS);
			return datagram;
		}
		return JsonUtil.getObjectFromJson(cityJson, new TypeReference<Datagram<SelectBox>>() {
		});
	}

	/**
	 * 获取区列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/zoneList", method = RequestMethod.GET)
	public Object getZoneList(RangeParams params, BindingResult result) {
		if (StringUtil.isBlank(params.getCityId())) {
			Datagram datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(MessageConstant.City_Id);
			datagram.setIsApp(false);
			return datagram;
		}
		String zoneJson = valueOpsCache.get(CommonConstant.KEY_REDIS_ZONE_PREFIX + params.getCityId());
		if (StringUtil.isBlank(zoneJson)) {
			Datagram<SelectBox> datagram = commonService.getZoneList(Integer.valueOf(params.getCityId()));
			valueOpsCache.set(CommonConstant.KEY_REDIS_ZONE_PREFIX + params.getCityId(),
					JsonUtil.getJsonFromObject(datagram), cookieMaxAge + 10, TimeUnit.SECONDS);
			return datagram;
		}
		return JsonUtil.getObjectFromJson(zoneJson, new TypeReference<Datagram<SelectBox>>() {
		});
	}

	/**
	 * 获取学校列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/siteList", method = RequestMethod.GET)
	public Object getSiteList(RangeParams params, BindingResult result) {
		if (StringUtil.isBlank(params.getZoneId())) {
			Datagram datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.STATUS_REQUEST_DATA_INVALID);
			datagram.setMsg(MessageConstant.Zone_Id);
			datagram.setIsApp(false);
			return datagram;
		}
		String siteJson = valueOpsCache.get(CommonConstant.KEY_REDIS_SITE_PREFIX + params.getZoneId());
		if (StringUtil.isBlank(siteJson)) {
			Datagram<SelectBox> datagram = commonService.getSiteList(Integer.valueOf(params.getZoneId()));
			valueOpsCache.set(CommonConstant.KEY_REDIS_SITE_PREFIX + params.getZoneId(),
					JsonUtil.getJsonFromObject(datagram), cookieMaxAge + 10, TimeUnit.SECONDS);
			return datagram;
		}
		return JsonUtil.getObjectFromJson(siteJson, new TypeReference<Datagram<SelectBox>>() {
		});
	}

	/**
	 * 根据关键字，模糊搜索学校
	 *
	 * @param siteName
	 * @return
	 */
	@RequestMapping(value = "/searchSiteByName", method = RequestMethod.GET)
	public Object searchSiteByName(String siteName) {
		List<SiteInfo> siteList = commonService.getSiteListByName(siteName);
		return DatagramHelper.getDatagram(siteList, CommonConstant.GLOBAL_STATUS_SUCCESS,
				CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	/**
	 * 根据cityID获取学校列表
	 *
	 * @param zoneId
	 * @return
	 */
	@RequestMapping(value = "/zone/siteList", method = RequestMethod.GET)
	public Object getSchoolList(Integer cityId) {
		String siteJson = valueOpsCache.get(CommonConstant.KEY_REDIS_SITE_BY_CITYID_PREFIX + cityId);
		if (StringUtil.isBlank(siteJson)) {
			SchoolInfo entry = commonService.getSchoolList(cityId);
			valueOpsCache.set(CommonConstant.KEY_REDIS_SITE_BY_CITYID_PREFIX + cityId,
					JsonUtil.getJsonFromObject(entry), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(entry, CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		}

		Datagram<SchoolInfo> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		datagram.setData(JsonUtil.getObjectFromJson(siteJson, new TypeReference<SchoolInfo>() {
		}));
		return datagram;
	}

	/**
	 * 获取店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value = "/dormShop", method = RequestMethod.GET)
	public Object getDormShop(Integer shopId) {
		if (shopId == null) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID, MessageConstant.Shop_Id);
		}
		String shopJson = valueOpsCache.get(CommonConstant.KEY_REDIS_SHOP + shopId);
		if (StringUtil.isBlank(shopJson)) {
			ShopMsg shopMsg = commonService.getDormShop(shopId);
			valueOpsCache.set(CommonConstant.KEY_REDIS_SHOP + shopId, JsonUtil.getJsonFromObject(shopMsg),
					cookieMaxAge + 10, TimeUnit.SECONDS);
			Datagram<ShopMsg> datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
			datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
			datagram.setIsApp(false);
			datagram.setData(shopMsg);
			return datagram;
		}
		Datagram<ShopMsg> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(false);
		datagram.setData(JsonUtil.getObjectFromJson(shopJson, new TypeReference<ShopMsg>() {
		}));
		return datagram;
	}

	/**
	 * 楼栋获取店铺
	 *
	 * @param dormentryId
	 * @return
	 */
	@RequestMapping(value = "/dormentrysShop", method = RequestMethod.GET)
	public Object findByDormEntryIds(Integer dormentryId) {
		if (dormentryId == null) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID, MessageConstant.Dormentry_Id);
		}
		String json = valueOpsCache.get(CommonConstant.KEY_REDIS_SHOP_BY_DORMENTRYID + dormentryId);
		if (StringUtil.isBlank(json)) {
			ShopInfos entry = commonService.getDormentrysShop(dormentryId);
			valueOpsCache.set(CommonConstant.KEY_REDIS_SHOP_BY_DORMENTRYID + dormentryId,
					JsonUtil.getJsonFromObject(entry), cookieMaxAge + 10, TimeUnit.SECONDS);
			Datagram<ShopInfos> datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
			datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
			datagram.setData(entry);
			datagram.setIsApp(false);
			return datagram;
		}
		return DatagramHelper.getDatagram(JsonUtil.getObjectFromJson(json, new TypeReference<ShopInfos>() {
		}), CommonConstant.GLOBAL_STATUS_SUCCESS, CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	/**
	 * 获取公寓/楼栋/信息
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/dormEntrys", method = RequestMethod.GET)
	public Object findDromentryBySiteId(@Valid DormShopParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage());
		}
		String json = valueOpsCache.get(CommonConstant.KEY_REDIS_DROMENTRYS_BY_SITEID + param.getSiteId());
		if (StringUtil.isBlank(json)) {
			Map<String, List<DormentryInfo>> entry = commonService.findDromentryBySiteId(param.getSiteId());
			valueOpsCache.set(CommonConstant.KEY_REDIS_DROMENTRYS_BY_SITEID + param.getSiteId(),
					JsonUtil.getJsonFromObject(entry), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(entry, CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		}
		return DatagramHelper
				.getDatagram(JsonUtil.getObjectFromJson(json, new TypeReference<Map<String, List<DormentryInfo>>>() {
				}), CommonConstant.GLOBAL_STATUS_SUCCESS, CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	/**
	 * 获取公寓/楼栋/店铺/信息
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/dormShops", method = RequestMethod.GET)
	public Object findDromShopsBySiteId(@Valid DormShopParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage());
		}

		String json = valueOpsCache.get(CommonConstant.KEY_REDIS_DORMSHOPS_BY_SITEID + param.getSiteId());
		if (StringUtil.isBlank(json)) {
			Map<String, List<DormentryInfo>> entry = commonService.findDromShopsBySiteId(param.getSiteId());
			valueOpsCache.set(CommonConstant.KEY_REDIS_DORMSHOPS_BY_SITEID + param.getSiteId(),
					JsonUtil.getJsonFromObject(entry), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(entry, CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		}
		return DatagramHelper
				.getDatagram(JsonUtil.getObjectFromJson(json, new TypeReference<Map<String, List<DormentryInfo>>>() {
				}), CommonConstant.GLOBAL_STATUS_SUCCESS, CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	/**
	 * 获取用户信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET)
	public Object userinfo_cache(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId) {
		String loginId = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
		// Login id信息取得
		LoginIdInfo loginIdInfo = JsonUtil.getObjectFromJson(loginId, LoginIdInfo.class);

		String loginIds = valueOpsCache.get(CommonConstant.KEY_REDIS_USER_PREFIX + loginIdInfo.getUid());

		UserInfo userInfo = JsonUtil.getObjectFromJson(loginIds, UserInfo.class);

		Datagram<UserInfo> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setData(userInfo);
		datagram.setIsApp(false);

		return datagram;
	}

	/**
	 * 获取店铺打印价格
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getDormShopPrice", method = RequestMethod.GET)
	public Object getDormShopPrice(@Valid DormShopPriceParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage());
		}
		String dormShopPriceJson = valueOpsCache.get(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShopId());
		if (StringUtil.isBlank(dormShopPriceJson)) {
			List<DormShopPrice> dormShopPrice = commonService.getDormShopPrice(param.getShopId());
			valueOpsCache.set(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShopId(),
					JsonUtil.getJsonFromObject(dormShopPrice), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(dormShopPrice, CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		}
		Datagram<List<DormShopPrice>> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setData(JsonUtil.getObjectFromJson(dormShopPriceJson, new TypeReference<List<DormShopPrice>>() {
		}));
		datagram.setIsApp(false);
		return datagram;
	}

	@Autowired
	private RabbitSender rabbitSender;
	@Autowired
	private UserApi userApi;
	private static boolean tmp = true;

	@RequestMapping("appmsg")
	public String appMsg(@RequestParam(required = false) String uid, @RequestParam(required = false) Integer code,
			@RequestParam String msg, @RequestParam(required = false) String title) {
//		 Message message = PushEntityHelper.buildPersistenceMessage
//		 (1, null, title,msg, null,1,null,null);
		if (tmp) {
			tmp = false;
			new Thread() {
				@Override
				public void run() {
					PushContent content = PushEntityHelper.buildContent(code == null ? 140 : code, title, msg, null);
					if (uid == null) {
						UserFilter filter = new UserFilter();
						Result<List<User>> result = userApi.getUserListByFilter(filter);
						if (result.getData() == null || result.getStatus() != 0) {
							throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "获取用户列表信息失败");
						}
						for (User user : result.getData()) {
							PushEntity pushEntity = PushEntityHelper.buildSingleToMobile(PushTargetEnum.STORE,
									user.getUid().toString(), content);
							rabbitSender.send(pushEntity);
						}
					} else {
						PushEntity pushEntity = PushEntityHelper.buildSingleToMobile(PushTargetEnum.STORE, uid,
								content);
						rabbitSender.send(pushEntity);
					}
					tmp = true;
				}
			}.run();
		}
		return "msg is sending";
	}
	@Autowired
	HttpClientRequest request;
//	@Scheduled(fixedRate = 30 * 60 * 1000)
	public void test(){
//		String rs=request.conn("http://print.59store.net/printapi/wechat/token?appId=wx2d5c264cdd7bb4e0&appSecret=e4693f6cf70514fa48aa1b5e9de1358c",CommonConstant.HTTP_METHOD_GET , null);
//		System.out.println(rs);
		try {
			URI uri = new URI("http://print.59store.net/printapi/wechat/token?appId=wx2d5c264cdd7bb4e0&appSecret=e4693f6cf70514fa48aa1b5e9de1358c");
			SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
			ClientHttpRequest chr = schr.createRequest(uri, HttpMethod.POST);
            //chr.getBody().write(param.getBytes());//body中设置请求参数
			ClientHttpResponse res = chr.execute();
			InputStream is = res.getBody(); //获得返回数据,注意这里是个流
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			while((str = br.readLine())!=null){
				System.out.println(str);//获得页面内容或返回内容
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	

}
