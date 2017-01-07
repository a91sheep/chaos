/**
 *
 */
package com.store59.printapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.base.common.api.CityApi;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.api.ProvinceApi;
import com.store59.base.common.api.SiteApi;
import com.store59.base.common.api.ZoneApi;
import com.store59.base.common.model.City;
import com.store59.base.common.model.Dormentry;
import com.store59.base.common.model.Province;
import com.store59.base.common.model.Site;
import com.store59.base.common.model.Zone;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormAssetsApi;
import com.store59.dorm.common.api.DormEntryShopApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.api.DormShopTimeApi;
import com.store59.dorm.common.data.ConstDorm;
import com.store59.dorm.common.filter.DormAssetsFilter;
import com.store59.dorm.common.filter.DormEntryShopFilter;
import com.store59.dorm.common.filter.DormShopDeliveryFilter;
import com.store59.dorm.common.filter.DormShopFilter;
import com.store59.dorm.common.model.Dorm;
import com.store59.dorm.common.model.DormAssets;
import com.store59.dorm.common.model.DormEntryShop;
import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopDelivery;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.dorm.common.model.DormShopTime;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.DateUtil;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.common.http.HttpClientRequest;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.DeliveryTimeUtil;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.IamgesResize;
import com.store59.printapi.common.utils.ResultUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.SelectBox;
import com.store59.printapi.model.result.app.FreePicResult;
import com.store59.printapi.model.result.app.ShopDelivery;
import com.store59.printapi.model.result.app.ShopTimeInfo;
import com.store59.printapi.model.result.app.ShopTimeView;
import com.store59.printapi.model.result.common.DormShopDeliveryForApp;
import com.store59.printapi.model.result.common.DormShopDeliveryForPc;
import com.store59.printapi.model.result.common.DormShopInfo;
import com.store59.printapi.model.result.common.DormShopPriceApp;
import com.store59.printapi.model.result.common.DormentryInfo;
import com.store59.printapi.model.result.common.SchoolInfo;
import com.store59.printapi.model.result.common.ShopInfo;
import com.store59.printapi.model.result.common.ShopInfos;
import com.store59.printapi.model.result.common.ShopMsg;
import com.store59.printapi.model.result.common.ShopMsgForApp;
import com.store59.printapi.model.result.common.SiteInfo;
import com.store59.printapi.model.result.common.ZoneInfo;
import com.store59.printapi.model.trans.ResultTrans;

/**
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
@Service("commonService")
public class CommonService {
	@Autowired
	private ProvinceApi provinceService;
	@Autowired
	private CityApi cityService;
	@Autowired
	private ZoneApi zoneService;
	@Autowired
	private SiteApi siteService;
	@Autowired
	private DormentryApi dormentryService;
	@Autowired
	private DormEntryShopApi dormentryShopService;
	@Autowired
	private DormShopApi dormShopService;

	@Autowired
	private DormShopPriceApi dormShopPriceApi;

	@Autowired
	private DormShopTimeApi dormShopTimeApi;
	@Autowired
	DormShopApi dormShopApi;
	@Autowired
	private DormApi dormApi;
	@Autowired
	private DormAssetsApi dormAssetsApi;
	@Autowired
	private DormShopDeliveryApi dormShopDeliveryApi;
	private Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Value("${aliyun.oss.shopLogo.domainName}")
	private String shopLogoDomainName;
    @Autowired
    private OSSClient          ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String             bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String             domainName;
	public Datagram<SelectBox> getProvinceList() {
		Result<List<Province>> result = provinceService.getProvinceList();
		if (result.getStatus() != 0) {
			return DatagramHelper.getDatagram(result.getStatus(), result.getMsg());
		}
		SelectBox data = ResultTrans.transProvinceSelectBox(result.getData());
		return DatagramHelper.getDatagramWithSuccess(data);
	}

	public Datagram<SelectBox> getCityList(Integer provinceId) {
		Result<List<City>> result = cityService.getCityList(provinceId);
		if (result.getStatus() != 0) {
			return DatagramHelper.getDatagram(result.getStatus(), result.getMsg());
		}
		SelectBox data = ResultTrans.transCitySelectBox(result.getData());
		System.out.println(data);
		return DatagramHelper.getDatagramWithSuccess(data);
	}

	public Datagram<SelectBox> getZoneList(Integer cityId) {
		Result<List<Zone>> result = zoneService.getZoneList(cityId);
		if (result.getStatus() != 0) {
			return ResultUtil.getResult(result.getStatus(), result.getMsg());
		}
		SelectBox data = ResultTrans.transZoneSelectBox(result.getData());
		return DatagramHelper.getDatagramWithSuccess(data);
	}

	public Datagram<SelectBox> getSiteList(Integer zoneId) {
		Result<List<Site>> result = siteService.getSiteByZoneId(zoneId);
		if (result.getStatus() != 0) {
			return DatagramHelper.getDatagram(result.getStatus(), result.getMsg());
		}
		SelectBox data = ResultTrans.transSiteSelectBox(result.getData());
		return DatagramHelper.getDatagramWithSuccess(data);
	}

	public List<SiteInfo> getSiteListByName(String siteName) {
		Result<List<Site>> result = siteService.searchSiteList(siteName);
		if (result.getStatus() != 0 || result.getData() == null) {
			throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
					CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
		}
		List<SiteInfo> list = new ArrayList<>();
		for (Site site : result.getData()) {
			SiteInfo siteInfo = new SiteInfo();
			siteInfo.setSiteId(site.getSiteId());
			siteInfo.setSiteName(site.getSiteName());
			list.add(siteInfo);
		}
		return list;
	}

	public SchoolInfo getSchoolList(Integer cityId) {
		Result<List<Zone>> zoneresult = zoneService.getZoneList(cityId);
		List<ZoneInfo> zoneList = new ArrayList<>();

		for (Zone zone : zoneresult.getData()) {
			List<SiteInfo> siteList = new ArrayList<>();
			Result<List<Site>> siteresult = siteService.getSiteByZoneId(zone.getZoneId());
			ZoneInfo zoneInfo = new ZoneInfo();
			zoneInfo.setZoneName(zone.getName());
			for (Site site : siteresult.getData()) {
				SiteInfo siteInfo = new SiteInfo();
				siteInfo.setSiteId(site.getSiteId());
				siteInfo.setSiteName(site.getSiteName());
				siteList.add(siteInfo);
			}

			zoneInfo.setSites(siteList);
			zoneList.add(zoneInfo);

		}
		SchoolInfo schoolInfo = new SchoolInfo();
		schoolInfo.setSchools(zoneList);

		return schoolInfo;
	}

	public List<ShopDelivery> getDormShopDeliveries(Integer shopId) {
		List<ShopDelivery> retList = new ArrayList<>();

		Result<DormShop> result = dormShopService.findByShopId(shopId, true, false, false, true);
		if (result == null || result.getStatus() != 0) {
			throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
					CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
		}
		if (result.getData() == null) {
			throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "id为" + shopId + "的店铺不存在!");
		}

		// 对营业时间进行处理
		DeliveryTimeUtil util = new DeliveryTimeUtil();
		Datagram<ShopTimeView> datagram = util.getExpectTimeList(result);
		List<ShopTimeInfo> delivery_times = new ArrayList<>();
		List<ShopTimeInfo> delivery_times2 = new ArrayList<>();

		if (datagram.getData() != null) {
			delivery_times = datagram.getData().getDelivery_times();
		}

		for (ShopTimeInfo shopTimeInfo : delivery_times) {
			ShopTimeInfo shopTimeInfo1 = new ShopTimeInfo();
			BeanUtils.copyProperties(shopTimeInfo, shopTimeInfo1);
			delivery_times2.add(shopTimeInfo1);
		}

		List<DormShopDelivery> dormShopDeliveries = result.getData().getDormShopDeliveries();
		for (DormShopDelivery dormShopDelivery : dormShopDeliveries) {
			if (dormShopDelivery.getStatus().byteValue() == 1) {// 启用
				ShopDelivery delivery = new ShopDelivery();
				delivery.setSend_type(dormShopDelivery.getMethod());
				delivery.setDelivery_amount(dormShopDelivery.getCharge());
				delivery.setFree_delivery_amount(dormShopDelivery.getThreshold());
				// TODO 配送方式说明
				if (CommonConstant.DELIVERY_TYPE_DORMER == dormShopDelivery.getMethod().byteValue()) {
					// 店长配送
					if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
							&& 1 == dormShopDelivery.getThresholdSwitch().intValue()) {
						// 满减配送开启状态
						delivery.setDescription("订单满" + dormShopDelivery.getThreshold().intValue() + "元免配送费");
					}
				} else {
					// 上门自取
					if (dormShopDelivery.getAutoConfirmSwitch() != null && dormShopDelivery.getAutoConfirm() != null
							&& 1 == dormShopDelivery.getAutoConfirmSwitch().intValue()) {
						// 订单打印完成自动确认 开启状态
						delivery.setDescription("订单打印完成" + dormShopDelivery.getAutoConfirm() + "小时后自动确认完成");
					}

					// 上门自取需要返回自取时间和地点
					List<DormShopTime> dormShopTimes = result.getData().getDormShopTimes();
					delivery.setPick_address(dormShopDelivery.getAddress());
					StringBuilder sb = new StringBuilder();
					for (DormShopTime shopTime : dormShopTimes) {
						if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
							int left_start = shopTime.getStartTime() % 100;
							int pre_start = (shopTime.getStartTime() - left_start) / 100;
							if (left_start < 10) {
								sb.append(pre_start + ":0" + left_start + "~");
							} else {
								sb.append(pre_start + ":" + left_start + "~");
							}

							int left_end = shopTime.getEndTime() % 100;
							int pre_end = (shopTime.getEndTime() - left_end) / 100;
							if (left_end < 10) {
								sb.append(pre_end + ":0" + left_end + "  ");
							} else {
								sb.append(pre_end + ":" + left_end + "  ");
							}
						}
					}
					delivery.setPick_time_string(sb.toString());
				}
				// 对获取的配送时间进行处理
				retList.add(delivery);
			}
		}
		if (retList.size() == 0) {

		} else if (retList.size() == 1) {
			retList.get(0).setDelivery_times(delivery_times);
		} else {
			retList.get(0).setDelivery_times(delivery_times);
			retList.get(1).setDelivery_times(delivery_times2);
		}
		return retList;
	}

	public ShopMsg getDormShop(Integer shopId) {
		boolean isTime = true;
		boolean isDormEntry = false;
		boolean isPrice = true;
		boolean isDelivery = true;
		Result<DormShop> result = dormShopService.findByShopId(shopId, isTime, isDormEntry, isPrice, isDelivery);
		ShopMsg msg = new ShopMsg();
		if (result == null || result.getStatus() != 0 || result.getData() == null) {
			if (result != null) {
				throw new BaseException(result.getStatus(), result.getMsg());
			}
			return msg;
		}

		msg.setShopId(result.getData().getShopId());
		msg.setShopName(result.getData().getName());
		if (!StringUtil.isBlank(result.getData().getLogo())) {
			msg.setShopLogo(shopLogoDomainName + result.getData().getLogo());
		}
		msg.setShopNotice(result.getData().getNotice());
		/**
		 * 营业时间处理
		 */
		List<DormShopDeliveryForPc> dormShopDeliveryForPcList = getDormshopDelForPC(
				result.getData().getDormShopDeliveries(), result.getData());
		msg.setDormShopDeliveries(dormShopDeliveryForPcList);
		msg.setFreeship_amount(result.getData().getFreeshipAmount());
		msg.setCross_building_dist_switch(result.getData().getCrossBuildingDistSwitch());
		// msg.setDormShopPrices(result.getData().getDormShopPrices());
		List<DormShopPrice> priceList = new ArrayList<>();
		List<DormShopPrice> shopPrices = result.getData().getDormShopPrices();
		// 价格过滤
		for (DormShopPrice price : shopPrices) {
			if (price.getUnitPrice() != null
					&& (price.getUnitPrice().doubleValue() != 0d || price.getUnitPrice().doubleValue() != 0.0)) {
				priceList.add(price);
			}
		}
		msg.setDormShopPrices(priceList);

		Result<List<DormShopTime>> shopTimeResult = dormShopTimeApi.findByShopIds(Arrays.asList(shopId));
		if (shopTimeResult == null || shopTimeResult.getStatus() != 0 || shopTimeResult.getData() == null) {
			throw new BaseException(shopTimeResult.getStatus(), shopTimeResult.getMsg());
		}

		if (result.getData().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_OPEN) {
			msg.setBusinessStatus(CommonConstant.SHOP_STATUS_1);
		} else if (result.getData().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_AUTO) {
			boolean is_open = false;
			List<DormShopTime> shopTimes = shopTimeResult.getData();
			for (DormShopTime shopTime : shopTimes) {
				if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
					Date d = new Date();
					int now = getTimeInt(d);
					if (now >= shopTime.getStartTime() && now <= shopTime.getEndTime()) {
						is_open = true;
						break;
					}
				}
			}
			if (is_open) {
				msg.setBusinessStatus(CommonConstant.SHOP_STATUS_1); // 营业中
			} else {
				msg.setBusinessStatus(CommonConstant.SHOP_STATUS_2); // 可预定
			}
		} else {
			msg.setBusinessStatus(CommonConstant.SHOP_STATUS_0);
		}
		return msg;
	}

	/**
	 * 获取店铺价格
	 *
	 * @param shopId
	 * @return
	 */
	public List<DormShopPrice> getDormShopPrice(Integer shopId) {
		Result<List<DormShopPrice>> dormShopPriceList = dormShopPriceApi.findByShopId(shopId);
		if (dormShopPriceList == null || dormShopPriceList.getStatus() != 0 || dormShopPriceList.getData() == null) {
			if (dormShopPriceList != null) {
				throw new AppException(dormShopPriceList.getStatus(), dormShopPriceList.getMsg());
			}
		}
		return dormShopPriceList.getData();
	}

	/**
	 * 获取楼栋信息
	 *
	 * @param siteId
	 * @return
	 */
	public Map<String, List<DormentryInfo>> findDromentryBySiteId(Integer siteId) {
		Result<List<Dormentry>> dormentryData = dormentryService.getDormentryListBySiteId(siteId);
		if (dormentryData == null || dormentryData.getStatus() != 0 || dormentryData.getData() == null) {
			if (dormentryData != null) {
				throw new BaseException(dormentryData.getStatus(), dormentryData.getMsg());
			} else {
				return null;
			}
		}
		Map<String, List<DormentryInfo>> apart = this.assembleDormentry(dormentryData.getData());
		return apart;
	}

	/**
	 * 组装楼栋信息
	 *
	 * @return
	 */
	private Map<String, List<DormentryInfo>> assembleDormentry(List<Dormentry> dormentryList) {
		Map<String, List<DormentryInfo>> apart = new HashMap<>();

		// 排序楼栋,按照sort值降序排，然后按照dormentryid升序排
		dormentryList.sort((x, y) -> {
			int value = y.getSort().compareTo(x.getSort());
			if (value == 0) {
				value = x.getDormentryId().compareTo(y.getDormentryId());
			}
			return value;
		});

		for (Dormentry dormentry : dormentryList) {
			if (apart.get(dormentry.getAddress1()) == null || apart.get(dormentry.getAddress1()).size() == 0) {
				// ApartInfo apartInfo = new ApartInfo();
				// apartInfo.setApartName(dormentry.getAddress1());
				List<DormentryInfo> list = new ArrayList<>();
				DormentryInfo dormentryInfoResult = new DormentryInfo();
				dormentryInfoResult.setDormentryId(dormentry.getDormentryId());
				dormentryInfoResult.setDormentryName(dormentry.getAddress2());
				dormentryInfoResult.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_0); // 未开通
				list.add(dormentryInfoResult);
				// apartInfo.setDormentryList(list);
				apart.put(dormentry.getAddress1(), list);
			} else {
				List<DormentryInfo> list = apart.get(dormentry.getAddress1());
				DormentryInfo dormentryInfoResult = new DormentryInfo();
				dormentryInfoResult.setDormentryId(dormentry.getDormentryId());
				dormentryInfoResult.setDormentryName(dormentry.getAddress2());
				dormentryInfoResult.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_0); // 未开通
				list.add(dormentryInfoResult);
			}
		}
		return apart;
	}

	// 组装店铺信息
	private DormShopInfo assembleDormShop(DormShopFilter filter) {
		DormShopInfo info = new DormShopInfo();
		Result<List<DormShop>> dormShopResult = dormShopService.findByFilter(filter);
		if (dormShopResult == null || dormShopResult.getStatus() != 0 || dormShopResult.getData() == null) {
			if (dormShopResult != null) {
				throw new BaseException(dormShopResult.getStatus(), dormShopResult.getMsg());
			} else {
				return null;
			}
		}

		Map<Integer, DormShop> dormShopMap = new HashMap<>();
		for (DormShop dormShop : dormShopResult.getData()) {
			dormShopMap.put(dormShop.getShopId(), dormShop);
		}

		Set<Integer> dormIdSet = dormShopResult.getData().parallelStream().map(DormShop::getDormId)
				.collect(Collectors.toSet());
		if (CollectionUtils.isEmpty(dormIdSet)) {
			throw new CommonException("店铺中没有找到店长信息");
		}
		info.setDormShopMap(dormShopMap);
		info.setDormIdList(new ArrayList<Integer>(dormIdSet));
		return info;
	}

	/**
	 * 获取楼栋&店铺信息
	 *
	 * @param siteId
	 * @return
	 */
	public Map<String, List<DormentryInfo>> findDromShopsBySiteId(Integer siteId) {
		Result<List<Dormentry>> dormentryData = dormentryService.getDormentryListBySiteId(siteId);
		if (dormentryData == null || dormentryData.getStatus() != 0 || dormentryData.getData() == null) {
			if (dormentryData != null) {
				throw new BaseException(dormentryData.getStatus(), dormentryData.getMsg());
			} else {
				return null;
			}
		}
		Map<String, List<DormentryInfo>> apart = this.assembleDormentry(dormentryData.getData());

		Set<Integer> dormentryIdSet = dormentryData.getData().parallelStream().map(Dormentry::getDormentryId)
				.collect(Collectors.toSet());

		if (CollectionUtils.isEmpty(dormentryIdSet)) {
			return apart;
		}

		DormEntryShopFilter filter = new DormEntryShopFilter();
		filter.setDormentryIds(new ArrayList<Integer>(dormentryIdSet));
		filter.setShopType(ConstDorm.DORM_SHOP_TYPE_2);
		filter.setIsDeliveryShop(true);
		Result<List<DormEntryShop>> result = dormentryShopService.findByFilter(filter);
		logger.info("获取打印店DormEntryShop信息:" + JsonUtil.getJsonFromObject(result));
		if (result == null || result.getStatus() != 0 || result.getData() == null) {
			if (result != null) {
				throw new BaseException(result.getStatus(), result.getMsg());
			} else {
				return apart;
			}
		}

		if (result.getData().size() == 0) {
			return apart;
		}

		Set<Integer> shopIds = result.getData().parallelStream().map(DormEntryShop::getShopId)
				.collect(Collectors.toSet());

		// 已dormentryId为key,List<DormShop>为value组装map
		Map<Integer, List<DormShop>> dormShopMap = new HashMap<>();
		for (DormEntryShop des : result.getData()) {
			if (des.getDeliverShop() != null) { // 过滤出打印店
				if (dormShopMap.get(des.getDormentryId()) != null && dormShopMap.get(des.getDormentryId()).size() > 0) {
					dormShopMap.get(des.getDormentryId()).add(des.getDeliverShop());
				} else {
					List<DormShop> dormShopList = new ArrayList<>();
					dormShopList.add(des.getDeliverShop());
					dormShopMap.put(des.getDormentryId(), dormShopList);
				}
			}
		}

		/**
		 * 获取店铺的营业时间
		 */
		Result<List<DormShopTime>> shopTimeRet = dormShopTimeApi.findByShopIds(new ArrayList<Integer>(shopIds));
		logger.info("店铺营业时间:" + JsonUtil.getJsonFromObject(shopTimeRet));
		Map<Integer, List<DormShopTime>> shopTimeMap = new HashMap<>();
		for (DormShopTime shopTime : shopTimeRet.getData()) {
			if (shopTime.getTimeSwitch().byteValue() == 1) { // 时间状态开启
				if (shopTimeMap.get(shopTime.getShopId()) != null && shopTimeMap.get(shopTime.getShopId()).size() > 0) {
					shopTimeMap.get(shopTime.getShopId()).add(shopTime);
				} else {
					List<DormShopTime> shopTimeList = new ArrayList<>();
					shopTimeList.add(shopTime);
					shopTimeMap.put(shopTime.getShopId(), shopTimeList);
				}
			}
		}

		// 取出营业中的店铺
		// Map<Integer, Integer> yeShop = null;
		// if (!CollectionUtils.isEmpty(shopIds)) {
		// DormShopFilter dsFilter = new DormShopFilter();
		// dsFilter.setShopIds(new ArrayList<Integer>(shopIds));
		// /**
		// * 此处的营业中的店铺获取的应该包含status=1和2的,即开店和自动的,以后只获取状态为2的了,没有1的,和服务端确认
		// */
		// dsFilter.setIsOpen(true);
		// Result<List<DormShop>> yeShopResult =
		// dormShopService.findByFilter(dsFilter);
		// if (yeShopResult == null || yeShopResult.getStatus() != 0 ||
		// yeShopResult.getData() == null) {
		// if (yeShopResult != null) {
		// throw new BaseException(yeShopResult.getStatus(),
		// yeShopResult.getMsg());
		// }
		// }
		// yeShop =
		// yeShopResult.getData().parallelStream().collect(Collectors.toMap(DormShop::getShopId,
		// (p) -> p.getShopId()));
		// }

		// 拼接剩余字段
		// for (Map.Entry<String, List<DormentryInfo>> entry : apart.entrySet())
		// {
		// for (DormentryInfo dormentry : entry.getValue()) {
		// List<DormShop> shopList =
		// dormShopMap.get(dormentry.getDormentryId());
		//
		// if (!CollectionUtils.isEmpty(shopList)) {
		// for (DormShop ds : shopList) {
		// if (yeShop != null && yeShop.get(ds.getShopId()) != null &&
		// yeShop.get(ds.getShopId()).intValue() == ds.getShopId().intValue()) {
		// dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_1);
		// //营业中
		// break;
		// } else {
		// dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_2);
		// //休息中
		// }
		// }
		// } else {
		// dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_0);
		// //未开通
		// }
		// //dormentry.setDormShopList(shopList);
		// }
		// }
		for (Map.Entry<String, List<DormentryInfo>> entry : apart.entrySet()) {
			for (DormentryInfo dormentry : entry.getValue()) {
				List<DormShop> shopList = dormShopMap.get(dormentry.getDormentryId());

				if (!CollectionUtils.isEmpty(shopList)) {
					boolean hasOpen = false; // 包含营业中
					boolean hasReserve = false; // 包含可预定
					boolean hasRest = false; // 包含休息中
					for (DormShop ds : shopList) {
						if (ds.getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_OPEN) {
							dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_1);
							hasOpen = true;
							break;
						} else if (ds.getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_AUTO) {
							if (shopTimeMap.get(ds.getShopId()) != null && shopTimeMap.get(ds.getShopId()).size() > 0) {
								List<DormShopTime> shopTimes = shopTimeMap.get(ds.getShopId());
								for (DormShopTime shopTime : shopTimes) {
									if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
										Date d = new Date();
										int now = getTimeInt(d);
										if (now >= shopTime.getStartTime() && now <= shopTime.getEndTime()) {
											hasOpen = true;
											break;
										} else {
											hasReserve = true;
										}
									}
								}
							}
						} else if (ds.getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_CLOSE) {
							hasRest = true; // 休息中
						}
					}
					if (hasOpen) {
						dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_1);
					} else if (hasReserve) {
						dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_2);
					} else if (hasRest) {
						dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_3);
					}
				} else {
					dormentry.setDormentryStatus(CommonConstant.DORMENTRY_STATUS_0); // 未开通
				}
				// dormentry.setDormShopList(shopList);
			}
		}
		return apart;
	}

	public ShopInfos getDormentrysShop(Integer dormentryId) {
		ShopInfos shopInfos = new ShopInfos();

		DormEntryShopFilter filter = new DormEntryShopFilter();
		filter.setDormentryIds(Arrays.asList(dormentryId));
		filter.setShopType(ConstDorm.DORM_SHOP_TYPE_2);
		filter.setIsDeliveryShop(true);
		Result<List<DormEntryShop>> dormentryShopresult = dormentryShopService.findByFilter(filter);
		if (dormentryShopresult == null || dormentryShopresult.getStatus() != 0
				|| dormentryShopresult.getData() == null) {
			if (dormentryShopresult != null) {
				throw new BaseException(dormentryShopresult.getStatus(), dormentryShopresult.getMsg());
			} else {
				return shopInfos;
			}
		}

		List<ShopInfo> shopList = new ArrayList<>();
		List<Integer> shopIds = new ArrayList<>();
		for (DormEntryShop dormEntryShop : dormentryShopresult.getData()) {
			ShopInfo shopInfo = new ShopInfo();
			if (dormEntryShop.getDeliverShop() != null) {
				shopInfo.setShopId(dormEntryShop.getDeliverShop().getShopId());
				shopInfo.setShopName(dormEntryShop.getDeliverShop().getName());
				if (!StringUtil.isBlank(dormEntryShop.getDeliverShop().getLogo())) {
					shopInfo.setLogo(shopLogoDomainName + dormEntryShop.getDeliverShop().getLogo());
				} else {
					shopInfo.setLogo("");
				}

				Result<List<DormShopTime>> result = dormShopTimeApi
						.findByShopIds(Arrays.asList(dormEntryShop.getDeliverShop().getShopId()));
				if (result == null || result.getStatus() != 0 || result.getData() == null) {
					throw new BaseException(result.getStatus(), result.getMsg());
				}

				if (dormEntryShop.getDeliverShop().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_OPEN) {
					shopInfo.setBusinessStatus(CommonConstant.SHOP_STATUS_1);
				} else if (dormEntryShop.getDeliverShop().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_AUTO) {
					boolean is_open = false;
					List<DormShopTime> shopTimes = result.getData();
					for (DormShopTime shopTime : shopTimes) {
						if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
							Date d = new Date();
							int now = getTimeInt(d);
							if (now >= shopTime.getStartTime() && now <= shopTime.getEndTime()) {
								is_open = true;
								break;
							}
						}
					}
					if (is_open) {
						shopInfo.setBusinessStatus(CommonConstant.SHOP_STATUS_1); // 营业中
					} else {
						shopInfo.setBusinessStatus(CommonConstant.SHOP_STATUS_2); // 可预定
					}
				} else {
					shopInfo.setBusinessStatus(CommonConstant.SHOP_STATUS_0); // 休息中
				}
				shopInfo.setShopStatus(dormEntryShop.getSort()); // 塞入sort值,后面判断会用
				shopList.add(shopInfo);
				shopIds.add(shopInfo.getShopId());
			}
		}
		// TODO 过滤未买打印机的店长开始
		DormShopFilter dormShopFilter = new DormShopFilter();
		dormShopFilter.setShopIds(shopIds);
		Result<List<DormShop>> listDormShop = dormShopApi.findByFilter(dormShopFilter);
		if (listDormShop.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"查询dormShop数据出错" + JsonUtil.getJsonFromObject(listDormShop));
		}
		List<Integer> dormIdList = new ArrayList<>();
		for (DormShop ds : listDormShop.getData()) {
			dormIdList.add(ds.getDormId());
		}
		DormAssetsFilter assetsFilter = new DormAssetsFilter();
		assetsFilter.setDormIdList(dormIdList);
		Result<List<DormAssets>> assetsList = dormAssetsApi.findByFilter(assetsFilter);
		if (assetsList.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"查询dormShop数据出错" + JsonUtil.getJsonFromObject(assetsList));
		}
		Set<Integer> shopIdsSet = new LinkedHashSet<>();
		for (DormAssets dAssets : assetsList.getData()) {
			for (DormShop ds : listDormShop.getData()) {
				if (ds.getDormId().equals(dAssets.getDormId())) {
					shopIdsSet.add(ds.getShopId());
					break;
				}
			}
		}
		List<ShopInfo> shopListWithout = new ArrayList<>();
		for (int i : shopIdsSet) {
			for (ShopInfo si : shopList) {
				if (si.getShopId() == i) {
					shopListWithout.add(si);
					break;
				}
			}
		}
		shopList = shopListWithout;
		// 过滤结束

		// 取出覆盖楼栋shopId
		List<Integer> shopIdList = shopList.parallelStream().map(ShopInfo::getShopId).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(shopIdList)) {
			return shopInfos;
		}

		// 查出只限自取的shopId
		DormShopDeliveryFilter dsdFilter = new DormShopDeliveryFilter();
		dsdFilter.setShopIds(shopIdList);
		dsdFilter.setMethod(ConstDorm.DORM_SHOP_DELIVERY_METHOD_1);
		dsdFilter.setStatus(ConstDorm.DORM_SHOP_DELIVERY_STATUS_0);

		Result<List<DormShopDelivery>> dsdListresult = dormShopDeliveryApi.findByFilter(dsdFilter);
		if (dsdListresult == null || dsdListresult.getStatus() != 0 || dsdListresult.getData() == null) {
			if (dormentryShopresult != null) {
				throw new BaseException(dsdListresult.getStatus(), dsdListresult.getMsg());
			} else {
				throw new CommonException("没有查到配送信息");
			}
		}
		Map<Integer, Integer> zqShopIds = dsdListresult.getData().parallelStream()
				.collect(Collectors.toMap(DormShopDelivery::getShopId, (p) -> p.getShopId()));

		List<ShopInfo> list = new ArrayList<>();
		for (ShopInfo si : shopList) {
			// 除了只限自取的,剩余的就是店长配送的
			// 59_dorm_shop_delivery表中没查到到店铺配送方式,会显示(送到楼下)
			if (si.getShopId() != null && zqShopIds.get(si.getShopId()) != null) {
				si.setShopStatus(CommonConstant.DELIVERY_STATUS_2); // 只限自取
			} else {
				if (si.getShopStatus() == 0) { // 用到前面塞入到sort值
					si.setShopStatus(CommonConstant.DELIVERY_STATUS_0); // 送到寝室
				} else {
					si.setShopStatus(CommonConstant.DELIVERY_STATUS_1); // 送到楼下
				}
			}
			list.add(si);
		}
		// 排序list
		// list.sort((x, y) -> {
		// int value = x.getShopStatus().compareTo(y.getShopStatus());
		// return value;
		// });

		// shopInfos.setShopInfos(list);

		// 店铺按照营业中>可预定>休息中排序，同等状态下按照本栋楼店>非本栋楼店排序；
		List<ShopInfo> sortedList = new ArrayList<>();
		// 先获取营业的商铺列表
		List<ShopInfo> workingList = new ArrayList<>();
		workingList = sortDormshopByBusinessStatus(list, CommonConstant.SHOP_STATUS_1);
		sortedList.addAll(workingList);

		// 获取可预定的商铺列表
		List<ShopInfo> autoList = new ArrayList<>();
		autoList = sortDormshopByBusinessStatus(list, CommonConstant.SHOP_STATUS_2);
		sortedList.addAll(autoList);

		// 获取休息中的商铺列表
		List<ShopInfo> restList = new ArrayList<>();
		restList = sortDormshopByBusinessStatus(list, CommonConstant.SHOP_STATUS_0);
		sortedList.addAll(restList);

		shopInfos.setShopInfos(sortedList);

		return shopInfos;
	}

	private List<ShopInfo> sortDormshopByBusinessStatus(List<ShopInfo> list, Byte businessStatus) {
		List<ShopInfo> sortedList = new ArrayList<>();
		sortedList = list.stream().filter(p -> p.getBusinessStatus().byteValue() == businessStatus.byteValue())
				.sorted((p1, p2) -> p1.getShopStatus().compareTo(p2.getShopStatus())).collect(Collectors.toList());

		return sortedList;
	}

	private List<DormShopDeliveryForPc> getDormshopDelForPC(List<DormShopDelivery> list, DormShop dormShop) {
		List<DormShopDeliveryForPc> pcList = new ArrayList<>();
		for (DormShopDelivery delivery : list) {
			DormShopDeliveryForPc pc = new DormShopDeliveryForPc();
			pc.setAddress(delivery.getAddress());
			pc.setShopId(delivery.getShopId());
			pc.setStatus(delivery.getStatus());
			pc.setMethod(delivery.getMethod());
			pc.setId(delivery.getId());
			pc.setContent(delivery.getContent());
			pc.setCharge(delivery.getCharge());
			pc.setAutoConfirmSwitch(delivery.getAutoConfirmSwitch());
			pc.setAutoConfirm(delivery.getAutoConfirm());
			pc.setThresholdSwitch(delivery.getThresholdSwitch());
			pc.setThreshold(delivery.getThreshold());
			pc.setArea(delivery.getArea());

			if (delivery.getMethod() == CommonConstant.DELIVERY_TYPE_DORMER) {
				// 组装配送时间
				DeliveryTimeUtil util = new DeliveryTimeUtil();
				Result<DormShop> result = new Result<>();
				result.setStatus(0);
				result.setData(dormShop);
				Datagram<ShopTimeView> datagram = util.getExpectTimeList(result);
				List<ShopTimeInfo> delivery_times = new ArrayList<>();

				if (datagram.getData() != null) {
					delivery_times = datagram.getData().getDelivery_times();
				}
				// 组装--店长配送
				pc.setDelivery_times(delivery_times);
			} else {
				// 组装--上门自取
				List<DormShopTime> list1 = dormShop.getDormShopTimes();
				StringBuilder sb = new StringBuilder();
				for (DormShopTime shopTime : list1) {
					if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
						int left_start = shopTime.getStartTime() % 100;
						int pre_start = (shopTime.getStartTime() - left_start) / 100;
						if (left_start < 10) {
							sb.append(pre_start + ":0" + left_start + "~");
						} else {
							sb.append(pre_start + ":" + left_start + "~");
						}

						int left_end = shopTime.getEndTime() % 100;
						int pre_end = (shopTime.getEndTime() - left_end) / 100;
						if (left_start < 10) {
							sb.append(pre_end + ":0" + left_end + "  ");
						} else {
							sb.append(pre_end + ":" + left_end + "  ");
						}
					}
				}
				pc.setPickTimeStr(sb.toString());
			}
			pcList.add(pc);
		}
		return pcList;
	}

	private int getTimeInt(Date d) {
		int hour = DateUtil.getHour(d);
		int minute = DateUtil.getMinute(d);
		StringBuilder time = new StringBuilder();
		if (minute < 10) {
			time.append(hour + "0" + minute);
		} else {
			time.append(hour + "" + minute);
		}
		int now = Integer.valueOf(time.toString());
		return now;
	}

	public ShopMsgForApp getDormShopApp(Integer shopId) {
		boolean isTime = true;
		boolean isDormEntry = false;
		boolean isPrice = true;
		boolean isDelivery = true;
		Result<DormShop> result = dormShopService.findByShopId(shopId, isTime, isDormEntry, isPrice, isDelivery);
		ShopMsgForApp msg = new ShopMsgForApp();
		if (result == null || result.getStatus() != 0 || result.getData() == null) {
			if (result != null) {
				throw new BaseException(result.getStatus(), result.getMsg());
			}
			return msg;
		}

		msg.setShop_id(result.getData().getShopId());
		msg.setShop_name(result.getData().getName());
		if (!StringUtil.isBlank(result.getData().getLogo())) {
			msg.setShop_logo(shopLogoDomainName + result.getData().getLogo());
		}
		msg.setShop_notice(result.getData().getNotice());
		/**
		 * 营业时间处理
		 */
		List<DormShopDeliveryForApp> dormShopDeliveryForAppList = getDormshopDelForApp(
				result.getData().getDormShopDeliveries(), result.getData());
		msg.setDorm_shop_deliveries(dormShopDeliveryForAppList);
		msg.setFreeship_amount(result.getData().getFreeshipAmount());
		msg.setCross_building_dist_switch(result.getData().getCrossBuildingDistSwitch());
		// msg.setDormShopPrices(result.getData().getDormShopPrices());
		List<DormShopPriceApp> priceList = new ArrayList<>();
		List<DormShopPrice> shopPrices = result.getData().getDormShopPrices();
		// 价格过滤
		for (DormShopPrice price : shopPrices) {
			if (price.getUnitPrice() != null
//					&& (price.getUnitPrice().doubleValue() != 0d || price.getUnitPrice().doubleValue() != 0.0)
					) {
				DormShopPriceApp dormShopPriceApp = new DormShopPriceApp();
				if(price.getType().byteValue()==PrintConst.PRINT_TYPE_BW_SINGLE){
					dormShopPriceApp.setName("A4黑白");
				}
				if(price.getType().byteValue()==PrintConst.PRINT_TYPE_COLOR_SINGLE){
					dormShopPriceApp.setName("A4彩色");
				}
				if(price.getType().byteValue()==PrintConst.PRINT_TYPE_PHOTO){
					dormShopPriceApp.setName("A6照片");
				}
				dormShopPriceApp.setShop_id(price.getShopId());
				dormShopPriceApp.setType(price.getType());
				dormShopPriceApp.setUnit_price(price.getUnitPrice());
				priceList.add(dormShopPriceApp);
			}
		}
		msg.setDorm_shop_prices(priceList);

		Result<List<DormShopTime>> shopTimeResult = dormShopTimeApi.findByShopIds(Arrays.asList(shopId));
		if (shopTimeResult == null || shopTimeResult.getStatus() != 0 || shopTimeResult.getData() == null) {
			throw new BaseException(shopTimeResult.getStatus(), shopTimeResult.getMsg());
		}

		if (result.getData().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_OPEN) {
			msg.setBusiness_status(CommonConstant.SHOP_STATUS_1);
		} else if (result.getData().getStatus().byteValue() == ConstDorm.DORM_SHOP_STATUS_AUTO) {
			boolean is_open = false;
			List<DormShopTime> shopTimes = shopTimeResult.getData();
			for (DormShopTime shopTime : shopTimes) {
				if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
					Date d = new Date();
					int now = getTimeInt(d);
					if (now >= shopTime.getStartTime() && now <= shopTime.getEndTime()) {
						is_open = true;
						break;
					}
				}
			}
			if (is_open) {
				msg.setBusiness_status(CommonConstant.SHOP_STATUS_1); // 营业中
			} else {
				msg.setBusiness_status(CommonConstant.SHOP_STATUS_2); // 可预定
			}
		} else {
			msg.setBusiness_status(CommonConstant.SHOP_STATUS_0);
		}
		return msg;
	}

	private List<DormShopDeliveryForApp> getDormshopDelForApp(List<DormShopDelivery> list, DormShop dormShop) {
		List<DormShopDeliveryForApp> pcList = new ArrayList<>();
		for (DormShopDelivery delivery : list) {
			DormShopDeliveryForApp pc = new DormShopDeliveryForApp();
			pc.setAddress(delivery.getAddress());
			pc.setShop_id(delivery.getShopId());
			pc.setStatus(delivery.getStatus());
			pc.setMethod(delivery.getMethod());
			pc.setId(delivery.getId());
			pc.setContent(delivery.getContent());
			pc.setCharge(delivery.getCharge());
			pc.setAuto_confirm_switch(delivery.getAutoConfirmSwitch());
			pc.setAuto_confirm(delivery.getAutoConfirm());
			pc.setThreshold_switch(delivery.getThresholdSwitch());
			pc.setThreshold(delivery.getThreshold());
			pc.setArea(delivery.getArea());

			if (delivery.getMethod() == CommonConstant.DELIVERY_TYPE_DORMER) {
				// 组装配送时间
				DeliveryTimeUtil util = new DeliveryTimeUtil();
				Result<DormShop> result = new Result<>();
				result.setStatus(0);
				result.setData(dormShop);
				Datagram<ShopTimeView> datagram = util.getExpectTimeList(result);
				List<ShopTimeInfo> delivery_times = new ArrayList<>();

				if (datagram.getData() != null) {
					delivery_times = datagram.getData().getDelivery_times();
				}
				// 组装--店长配送
				pc.setDelivery_times(delivery_times);
			} else {
				// 组装--上门自取
				List<DormShopTime> list1 = dormShop.getDormShopTimes();
				StringBuilder sb = new StringBuilder();
				for (DormShopTime shopTime : list1) {
					if (shopTime.getTimeSwitch() == ConstDorm.DORM_SHOP_TIME_SWITCH_OPEN) {
						int left_start = shopTime.getStartTime() % 100;
						int pre_start = (shopTime.getStartTime() - left_start) / 100;
						if (left_start < 10) {
							sb.append(pre_start + ":0" + left_start + "~");
						} else {
							sb.append(pre_start + ":" + left_start + "~");
						}

						int left_end = shopTime.getEndTime() % 100;
						int pre_end = (shopTime.getEndTime() - left_end) / 100;
						if (left_start < 10) {
							sb.append(pre_end + ":0" + left_end + "  ");
						} else {
							sb.append(pre_end + ":" + left_end + "  ");
						}
					}
				}
				pc.setPick_time_str(sb.toString());
			}
			pcList.add(pc);
		}
		return pcList;
	}
	public Datagram<FreePicResult> freePic(Integer shopId) {
		FreePicResult freePic=new FreePicResult();
		String qrCode=null;
		// 判断自动打印状态
		Result<DormShop> result = dormShopApi.findByShopId(shopId, false, false, false, false);
		String extension = result.getData().getExtension();
		DormShop dormShop=result.getData();
		Integer dormId=result.getData().getDormId();
		if (StringUtil.isTrimBlank(extension)) {
			logger.error("打印店shopId:{}调用dormShopApi.findByShopId获取的extension字段为null", result.getData().getShopId());
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "获取dormshop的extension字段为空");
		} else {
			JSONObject obj = JSONObject.parseObject(extension);
			qrCode = obj.getString("QRCode");

			if (StringUtil.isBlank(qrCode)) {
				// 如果店长的免费打印二维码为空,则调用第三方接口生成二维码
				qrCode = generateQRCode(dormId);
				if (!StringUtil.isBlank(qrCode)) {
					obj.put("QRCode", qrCode);
					// 存入数据库中
					dormShop.setExtension(obj.toString());
					Result<Boolean> booleanResult = dormShopApi.update(dormShop);
					if (booleanResult.getStatus() != 0 || booleanResult.getData() == null
							|| booleanResult.getData().equals(false)) {
						logger.error("dormShopApi.update by shopId:{} failed, result:{}", dormShop.getShopId(),
								JsonUtil.getJsonFromObject(booleanResult));
					}
				}
			}
		}
		if (!StringUtil.isBlank(qrCode)){
			freePic.setHas_opend((byte)1);
			try{
				//生成的图片上传oss
				BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(IamgesResize.createImage(qrCode, dormShop.getName()));
				//---------文件保存---------
				String conver_key = FileUtil.upload(multipartFile, "image/png", "image.png", ossClient, bucketName);
				String img_path = domainName + "/" + conver_key;
				freePic.setUrl(img_path);
			}catch(Exception e){
				freePic.setUrl(qrCode);
			}
		}else{
			freePic.setHas_opend((byte)0);
		}
		//免费照片打印下架
		freePic.setHas_opend((byte)0);

		Result<Dorm> dorm=dormApi.getDorm(dormId);
		freePic.setPhone(dorm.getData().getPhone());
		return DatagramHelper.getDatagramWithSuccess(freePic);
	}

	@Autowired
	private HttpClientRequest httpClientRequest;
	
	private String generateQRCode(Integer dormId) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("dorm_id", String.valueOf(dormId)));
		String ret = "";
		try {
			ret = httpClientRequest.conn(CommonConstant.PATH_GENERATE_QRCODE, CommonConstant.HTTP_METHOD_GET, params);
		} catch (Exception e) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,"生成二维码失败："+e.getMessage());
		}
		Map<String, Object> datagramWork = JsonUtil.getObjectFromJson(ret, new TypeReference<Map<String, Object>>() {
		});
		if (!(boolean) datagramWork.get("status")) {
			return null;
		}
		return (String) datagramWork.get("qr");
	}
}
