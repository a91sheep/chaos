package com.store59.printapi.controller.gala;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormEntryShopApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.model.Dorm;
import com.store59.dorm.common.model.DormEntryShop;
import com.store59.dorm.common.model.DormShop;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.UserInfo;
import com.store59.stock.common.dto.WarehouseDTO;
import com.store59.stock.common.enums.BusiTypeEnum;
import com.store59.stock.common.filter.DormWarehouseFilter;
import com.store59.stock.common.remoting.DormWarehouseService;
import com.store59.stock.common.remoting.WarehouseService;

@RestController
@RequestMapping("dorm")
public class DormApplyController {

	@Autowired
	DormApi dormApi;
	@Autowired
	DormWarehouseService dormWareService;
	@Autowired
	DormShopApi dormShopApi;
	@Autowired
	DormEntryShopApi dormEntryShopApi;
	@Autowired
	WarehouseService warehouseService;
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@RequestMapping("ismanager")
	public Object isManager(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
			@RequestParam(required = false) Long u_id) {
		String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
		LoginIdInfo loginIdInfo = new LoginIdInfo();
		try {
			loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
		} catch (Exception e) {
			throw new BaseException(CommonConstant.STATUS_CONVER_JSON, "获取用户信息失败");
		}
		Long uid = loginIdInfo.getUid();
		Result<Dorm> result = dormApi.getDormByUid(uid);
		if (result.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取店长信息失败" + JsonUtil.getJsonFromObject(result));
		}
		if (result.getData() == null) {

		} else {
			Datagram<?> data = new Datagram<>();
			data.setStatus(-1);
			data.setMsg("您已经是店长！");
			return data;
		}
		return DatagramHelper.getDatagramWithSuccess();
	}

	@RequestMapping("apply")
	public Object dormTest(@CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId,
			@RequestParam Integer siteId, @RequestParam String name, @RequestParam Integer gender,
			@RequestParam Integer dormentryId, @RequestParam String address) {
		String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
		LoginIdInfo loginIdInfo = new LoginIdInfo();
		try {
			loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
		} catch (Exception e) {
			throw new BaseException(CommonConstant.STATUS_CONVER_JSON, "获取用户信息失败");
		}
		Long uid = loginIdInfo.getUid();
		String userStr = valueOpsCache.get(CommonConstant.KEY_REDIS_USER_PREFIX + uid);
		UserInfo userInfo = JsonUtil.getObjectFromJson(userStr, UserInfo.class);
		name = name.length() > 15 ? name.substring(0, 15) : name;
		address = address.length() > 30 ? address.substring(0, 30) : address;
		Result<Dorm> result = dormApi.getDormByUid(uid);
		if (result.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取店长信息失败" + JsonUtil.getJsonFromObject(result));
		}
		if (result.getData() == null) {

		} else {
			Datagram<?> data = new Datagram<>();
			data.setStatus(-1);
			data.setMsg("您已经是店长！");
			return data;
		}
		// 判断学校是否有供应商 库存中心
		DormWarehouseFilter dormWarehouseFilter = new DormWarehouseFilter();
		dormWarehouseFilter.setSiteId(siteId);
		dormWarehouseFilter.setBusType(BusiTypeEnum.PRINT);
		Result<List<WarehouseDTO>> resDormWare = warehouseService.queryWarehouse(dormWarehouseFilter);

		if (resDormWare.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取供应商信息失败" + JsonUtil.getJsonFromObject(resDormWare));
		}

		if (resDormWare.getData() == null || resDormWare.getData().isEmpty()) {
			Datagram<?> data = new Datagram<>();
			data.setStatus(301);
			data.setMsg("抱歉，云印服务尚未覆盖贵校，<br/>暂时不能申请成为店长");
			return data;
		}
		// 开始生成店长数据
		Dorm dorm = new Dorm();
		dorm.setRole((byte) 8);
		dorm.setStatus((byte) 0);
		dorm.setIsPass((byte) 0);
		dorm.setName(name);
		dorm.setPhone(userInfo.getPhone());
		dorm.setEmail(userInfo.getEmail());
		dorm.setGender(gender.byteValue());
		dorm.setSid(siteId);
		dorm.setDormentryId(dormentryId);
		dorm.setFloor((byte) 0);
		dorm.setParentStaffId(0);
		dorm.setMoney(0d);
		dorm.setDeliveryAddress(address);
		dorm.setOverdraft(0d);
		dorm.setUid(uid);
		dorm.setSiteId(siteId);
		dorm.setAddTime((int) (System.currentTimeMillis() / 1000));
		Result<Dorm> dormResult = dormApi.addDorm(dorm);
		if (dormResult.getStatus() != 0 || dormResult.getData() == null) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"新增dorm数据出错" + JsonUtil.getJsonFromObject(dormResult));
		}
		DormShop dormShop = new DormShop();
		dormShop.setDormId(dormResult.getData().getDormId());
		dormShop.setLogo("");
		dormShop.setStatus((byte) 0);
		dormShop.setType((byte) 2);
		dormShop.setCrossBuildingDistSwitch((byte) 0);
		dormShop.setFreeshipAmount(0D);
		dormShop.setShipfee(0D);
		dormShop.setPersonalizedSignature("");
		dormShop.setNotice("");
		dormShop.setName(name + "的打印店");
		dormShop.setCrossBuildingDistSwitch((byte) 0);// 是否允许跨楼栋配送（0-关、1-开）
		Result<DormShop> dormShopResult = dormShopApi.addDormShop(dormShop);
		if (dormShopResult.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"新增dormShop数据出错" + JsonUtil.getJsonFromObject(dormShopResult));
		}
		DormEntryShop dormEntryShopDto = new DormEntryShop();
		dormEntryShopDto.setDormentryId(dormentryId);
		dormEntryShopDto.setShopId(dormShopResult.getData().getShopId());
		dormEntryShopDto.setSort((byte) 0);
		Result<DormEntryShop> dormEntryShopResult = dormEntryShopApi.insert(dormEntryShopDto);
		if (dormEntryShopResult.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"新增dormEntryShop数据出错" + JsonUtil.getJsonFromObject(dormEntryShopResult));
		}
		// 自动绑定经销商
		Result<Boolean> applyDormResult = dormWareService.applyDorm(dormResult.getData().getDormId(),
				BusiTypeEnum.PRINT, siteId);
		if (applyDormResult.getStatus() != 0 || !applyDormResult.getData()) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"自动绑定经销商失败" + JsonUtil.getJsonFromObject(applyDormResult));
		}
		Datagram<?> data = new Datagram<>();
		data.setStatus(0);
		data.setMsg("恭喜你申请成功！<br/>现在去店长后台看看吧～");
		return data;
	}

	boolean hasOpenedPrint(Dorm dormInfo) {
		if (dormInfo.getRole() == null) {
			return false;
		}
		if ((dormInfo.getRole() & 8) != 8) {
			return false;
		}
		return true;
	}

	@RequestMapping("test")
	public void test() {

		DormWarehouseFilter dormWarehouseFilter = new DormWarehouseFilter();
		dormWarehouseFilter.setSiteId(64);
		dormWarehouseFilter.setBusType(BusiTypeEnum.PRINT);
		Result<List<WarehouseDTO>> resDormWare = warehouseService.queryWarehouse(dormWarehouseFilter);

		if (resDormWare.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取供应商信息失败" + JsonUtil.getJsonFromObject(resDormWare));
		}

		if (resDormWare.getData() == null || resDormWare.getData().isEmpty()) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"该学校没有供应商！" + JsonUtil.getJsonFromObject(resDormWare));
		}
		Result<Boolean> applyDormResult = dormWareService.applyDorm(1539309575, BusiTypeEnum.PRINT, 64);
		if (applyDormResult.getStatus() != 0 || !applyDormResult.getData()) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"自动绑定经销商失败" + JsonUtil.getJsonFromObject(applyDormResult));
		}
		System.out.println(JsonUtil.getJsonFromObject(applyDormResult));
	}

}
