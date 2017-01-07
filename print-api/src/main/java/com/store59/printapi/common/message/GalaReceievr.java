package com.store59.printapi.common.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.store59.coupon.remoting.CouponService;
import com.store59.print.common.remoting.GalaPrintService;
import com.store59.user.common.api.UserApi;

@Component
public class GalaReceievr {

	@Autowired
	private GalaPrintService galaPrintService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserApi userApi;
	private static Logger logger = LoggerFactory.getLogger(GalaReceievr.class);
	
	void receiveGalaMessage(String message) throws Exception {
		// 生成统计记录
		try {
//			JSONObject json = JSONObject.parseObject(message);
//			if (json.get("couponType") != null && json.getInteger("couponType") == 1) {
//				String dormId = json.get("dormId") == null ? null : json.getString("dormId");
//				int couponAmounts = json.getIntValue("couponAmounts");
//				CouponAdd add = JsonUtil.getObjectFromJson(json.getString("data"), CouponAdd.class);
//				String phone=add.getPhone();
//				Result<User> userinfo = userApi.getUserByPhone(phone);
//				if (userinfo.getData() == null||userinfo.getData().getUid()==null) {
//					User user = new User();
//					user.setPhone(phone);
//					user.setNickname(phone);
//					user.setUname(phone);
//					user.setRegTime((int) (System.currentTimeMillis() / 1000));
//					userinfo = userApi.createUser(user);
//					if (userinfo.getStatus() != 0) {
//						logger.error("创建用户失败", JSONObject.toJSONString(userinfo));
//						throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "创建用户失败");
//					}
//				}
//				
//				GalaPrintRelation galaRelation = new GalaPrintRelation();
//				galaRelation.setCreateTime((int) (System.currentTimeMillis() / 1000));
//				if (dormId != null)
//					galaRelation.setDormId(Integer.parseInt(dormId));
//				galaRelation.setPhone(add.getPhone());
//				galaRelation.setUid(userinfo.getData().getUid());
//				Result<GalaPrintRelation> rs = galaPrintService.insert(galaRelation);
//				if (rs.getStatus() != 0) {
//					logger.error("gala relation insert error!"+JSONObject.toJSONString(rs));
//				}
//				// 发放优惠券
//				List<CouponAdd> list = new ArrayList<CouponAdd>();
//				for (int i = 0; i < couponAmounts; i++) {
//					add.setCode(null);
//					add.setUid(userinfo.getData().getUid());
//					list.add(add);
//				}
//				Result<List<Coupon>> coupon = couponService.addCouponList(list);
//				if (coupon.getStatus() != 0) {
//					logger.error("gala add coupon error!－gala"+JSONObject.toJSONString(coupon));
//				}
//			} else {
//				CouponAdd couponAdd = JsonUtil.getObjectFromJson(json.getString("data"), CouponAdd.class);
//				logger.info("调用优惠券成功：scope："+couponAdd.getScope()+"  手机："+couponAdd.getPhone());
//				Result<Coupon> coupon = couponService.addCoupon(couponAdd);
//				if (coupon.getStatus() != 0) {
//					logger.error("gala add coupon error!－others"+JSONObject.toJSONString(coupon));
//				}
//			}
		} catch (Exception e) {
			logger.error("优惠券异步发放失败"+e.getMessage());
		}
	}
}
