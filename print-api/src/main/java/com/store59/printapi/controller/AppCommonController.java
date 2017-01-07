package com.store59.printapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.dorm.common.model.DormShopPrice;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.param.common.AppShopPriceParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.app.ShopDeliveries;
import com.store59.printapi.model.result.app.ShopDelivery;
import com.store59.printapi.model.result.app.ShopPrice;
import com.store59.printapi.model.result.app.ShopPrint;
import com.store59.printapi.model.result.app.ShopSet;
import com.store59.printapi.model.result.common.ShopMsgForApp;
import com.store59.printapi.service.CommonService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
@RestController
@RequestMapping("/print/shop/*")
public class AppCommonController extends BaseController {
	@Autowired
	private CommonService commonService;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${cookie.max.age}")
	private int cookieMaxAge;

	/**
	 * 获取店铺打印价格
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/price/all", method = RequestMethod.GET)
	public Object getDormShopPriceAll(@Valid AppShopPriceParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage(), true);
		}
        List<DormShopPrice> dormShopPriceList = commonService.getDormShopPrice(param.getShop_id());
        List<ShopPrice> shopPriceList = new ArrayList<ShopPrice>();
        if (!CollectionUtils.isEmpty(dormShopPriceList)) {
            dormShopPriceList.stream().forEach(e -> {
                if (e.getType().equals(PrintConst.PRINT_TYPE_BW_DOUBLE) ||
                        e.getType().equals(PrintConst.PRINT_TYPE_COLOR_DOUBLE)) {
                    return;
                }
                ShopPrice shopPrice = new ShopPrice();
                shopPrice.setType(e.getType());
                shopPrice.setUnit_price(e.getUnitPrice());
                shopPriceList.add(shopPrice);
            });
        }
        return DatagramHelper.getDatagram(shopPriceList, CommonConstant.GLOBAL_STATUS_SUCCESS,
                CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
	}

	/**
	 * 获取店铺打印价格
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/formats", method = RequestMethod.GET)
	public Object getDormShopPrice(@Valid AppShopPriceParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage(), true);
		}
		String dormShopPriceJson = valueOpsCache
				.get(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShop_id());
		if (StringUtil.isBlank(dormShopPriceJson)) {
			List<DormShopPrice> dormShopPriceList = commonService.getDormShopPrice(param.getShop_id());
			valueOpsCache.set(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShop_id(),
					JsonUtil.getJsonFromObject(dormShopPriceList), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(getShopSet(dormShopPriceList), CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
		}

		Datagram<ShopSet> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(true);
		datagram.setData(
				getShopSet(JsonUtil.getObjectFromJson(dormShopPriceJson, new TypeReference<List<DormShopPrice>>() {
				})));
		return datagram;
	}

	/**
	 * 获取店铺配送信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/deliveries", method = RequestMethod.GET)
	public Object getShopDelivery(@Valid AppShopPriceParam param, BindingResult result) {

		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage(), true);
		}
		Integer shopId = param.getShop_id();
		String json = valueOpsCache.get(CommonConstant.KEY_REDIS_DORMSHOP_DELIVERY_ + param.getShop_id());
		if (StringUtil.isBlank(json)) {
			List<ShopDelivery> list = commonService.getDormShopDeliveries(shopId);
			valueOpsCache.set(CommonConstant.KEY_REDIS_DORMSHOP_DELIVERY_ + param.getShop_id(),
					JsonUtil.getJsonFromObject(list), cookieMaxAge + 10, TimeUnit.SECONDS);
			ShopDeliveries shopDeliveries = new ShopDeliveries();
			shopDeliveries.setDeliveries(list);
			return DatagramHelper.getDatagram(shopDeliveries, CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
		}
		List<ShopDelivery> list = JsonUtil.getObjectFromJson(json, new TypeReference<List<ShopDelivery>>() {
		});

		ShopDeliveries shopDeliveries = new ShopDeliveries();
		shopDeliveries.setDeliveries(list);
		Datagram<ShopDeliveries> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setData(shopDeliveries);
		datagram.setIsApp(true);
		return datagram;
	}

	public ShopSet getShopSet(List<DormShopPrice> dormShopPriceList) {
		ShopSet shopSet = new ShopSet();
		List<ShopPrice> shop_prices = new ArrayList<ShopPrice>();
		for (DormShopPrice dormShopPrice : dormShopPriceList) {
			// 暂时只返回单面打印
			if (dormShopPrice.getType() == PrintConst.PRINT_TYPE_BW_SINGLE
					|| dormShopPrice.getType() == PrintConst.PRINT_TYPE_COLOR_SINGLE) {
				ShopPrice shopPrice = new ShopPrice();
				shopPrice.setName(dormShopPrice.getName());
				shopPrice.setType(dormShopPrice.getType());
				shopPrice.setUnit_price(dormShopPrice.getUnitPrice());
				if (PrintConst.PRINT_TYPE_BW_SINGLE == dormShopPrice.getType()
						|| PrintConst.PRINT_TYPE_COLOR_SINGLE == dormShopPrice.getType()) {
					// 单面打印
					shopPrice.setPage_side(1);
				} else {
					// 双面打印
					shopPrice.setPage_side(2);
				}
				shop_prices.add(shopPrice);
			}
		}
		shopSet.setPrint_types(shop_prices);

		List<ShopPrint> shop_prints = new ArrayList<ShopPrint>();
		// 不缩印
		ShopPrint shopPrint1 = new ShopPrint();
		shopPrint1.setType(PrintConst.re_print_type_none);
		shopPrint1.setName("不缩印");
		shopPrint1.setReduced(1);
		shop_prints.add(shopPrint1);

		// 二合一
		ShopPrint shopPrint2 = new ShopPrint();
		shopPrint2.setType(PrintConst.re_print_type_two);
		shopPrint2.setName("一页二面");
		shopPrint2.setReduced(2);
		shop_prints.add(shopPrint2);

		// 四合一
		ShopPrint shopPrint3 = new ShopPrint();
		shopPrint3.setType(PrintConst.re_print_type_four);
		shopPrint3.setName("一页四面");
		shopPrint3.setReduced(4);
		shop_prints.add(shopPrint3);

		// 六合一
		ShopPrint shopPrint4 = new ShopPrint();
		shopPrint4.setType(PrintConst.re_print_type_six);
		shopPrint4.setName("一页六面");
		shopPrint4.setReduced(6);
		shop_prints.add(shopPrint4);

		// 九合一
		ShopPrint shopPrint5 = new ShopPrint();
		shopPrint5.setType(PrintConst.re_print_type_nine);
		shopPrint5.setName("一页九面");
		shopPrint5.setReduced(9);
		shop_prints.add(shopPrint5);

		shopSet.setReduced_types(shop_prints);

		return shopSet;
	}

	/**
	 * 获取店铺打印价格
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/picformats", method = RequestMethod.GET)
	public Object getPicPrice(@Valid AppShopPriceParam param, BindingResult result) {
		if (result.hasErrors()) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
					result.getAllErrors().get(0).getDefaultMessage(), true);
		}
		String dormShopPriceJson = valueOpsCache
				.get(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShop_id());
		if (StringUtil.isBlank(dormShopPriceJson)) {
			List<DormShopPrice> dormShopPriceList = commonService.getDormShopPrice(param.getShop_id());
			valueOpsCache.set(CommonConstant.KEY_REDIS_DORMSHOPPRICE_PREFIX + param.getShop_id(),
					JsonUtil.getJsonFromObject(dormShopPriceList), cookieMaxAge + 10, TimeUnit.SECONDS);
			return DatagramHelper.getDatagram(getPicShopSet(dormShopPriceList), CommonConstant.GLOBAL_STATUS_SUCCESS,
					CommonConstant.GLOBAL_STATUS_SUCCESS_MSG, true);
		}

		Datagram<ShopSet> datagram = new Datagram<ShopSet>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
		datagram.setIsApp(true);
		datagram.setData(
				getPicShopSet(JsonUtil.getObjectFromJson(dormShopPriceJson, new TypeReference<List<DormShopPrice>>() {
				})));
		return datagram;
	}

	private ShopSet getPicShopSet(List<DormShopPrice> dormShopPriceList) {
		ShopSet shopSet = new ShopSet();
		List<ShopPrice> shop_prices = new ArrayList<ShopPrice>();
		for (DormShopPrice dormShopPrice : dormShopPriceList) {
			// 暂时只6寸
			if (dormShopPrice.getType() == PrintConst.PRINT_TYPE_PHOTO) {
				ShopPrice shopPrice = new ShopPrice();
				shopPrice.setName(dormShopPrice.getName());
				shopPrice.setType(dormShopPrice.getType());
				shopPrice.setUnit_price(dormShopPrice.getUnitPrice());
				shop_prices.add(shopPrice);
			}
		}
		shopSet.setPrint_types(shop_prices);
		// 光面绒面
		List<ShopPrint> shop_prints = new ArrayList<ShopPrint>();
		ShopPrint shopPrint = new ShopPrint();
		shopPrint.setName("默认");
		shopPrint.setType(PrintConst.re_print_photo_default);
		shop_prints.add(shopPrint);
		shopSet.setReduced_types(shop_prints);
		return shopSet;
	}

	/**
	 * 获取店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value = "/dormShop", method = RequestMethod.GET)
	public Object getDormShop(@RequestParam Integer shopId) {
		if (shopId == null) {
			return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID, MessageConstant.Shop_Id);
		}
//		String shopJson = valueOpsCache.get(CommonConstant.KEY_REDIS_SHOP_APP + shopId);
//		if (StringUtil.isBlank(shopJson)) {
			ShopMsgForApp shopMsg = commonService.getDormShopApp(shopId);
			valueOpsCache.set(CommonConstant.KEY_REDIS_SHOP_APP + shopId, JsonUtil.getJsonFromObject(shopMsg),
					cookieMaxAge + 10, TimeUnit.SECONDS);
			Datagram<ShopMsgForApp> datagram = new Datagram<>();
			datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
			datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
			datagram.setData(shopMsg);
			return datagram;
//		}
//		Datagram<ShopMsgForApp> datagram = new Datagram<>();
//		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
//		datagram.setMsg(CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
//		datagram.setData(JsonUtil.getObjectFromJson(shopJson, new TypeReference<ShopMsgForApp>() {
//		}));
//		return datagram;
	}
	@RequestMapping("freePic")
	public Object freePic(@RequestParam Integer shopId){
		return commonService.freePic(shopId);
	}
}
