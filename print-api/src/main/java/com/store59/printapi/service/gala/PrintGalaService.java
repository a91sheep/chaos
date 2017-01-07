package com.store59.printapi.service.gala;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.store59.dto.common.enums.BizTypeEnum;
import com.store59.dto.common.order.OrderStatusEnum;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderContext;
import com.store59.order.common.service.dto.OrderCreateResultInfo;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.order.common.service.dto.OrderItemDTO;
import com.store59.order.common.service.enums.OrderSourceEnum;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import com.store59.order.common.service.facade.OrderCreateFacade;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.remoting.GalaPrintService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.result.Datagram;
import com.store59.user.common.api.UserApi;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年4月19日 上午12:14:17 活动service类
 */
@Service
public class PrintGalaService {

	@Value("${gala.print.expiredate}")
	private String expireDate;
	@Value("${gala.print.activedate}")
	private String activeDate;
	@Value("${gala.print.enterabletimes}")
	private String enterableTimes;
    @Value("${cookie.max.age}")
    private int cookieMaxAge;
	@Autowired
	private UserApi userApi;
	
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;
	@Autowired
	private OrderCreateFacade orderCreateFacade;
	@Autowired
	private BuyerOrderQueryFacade buyerOrderQueryFacade;

	private Logger logger = LoggerFactory.getLogger(PrintGalaService.class);
	
    @Autowired
    private GalaPrintService galaPrintService;
    
	private final short quantity=4;
	private final String itemId="printgalaitem01";
	
	private final String queueName = "print-gala";
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 验证用户参加活动资格
	 * 
	 * @param phone
	 * @return
	 */
	public Datagram<?> validateService(String phone,String dormId) throws Exception{
		logger.info("验证用户参加活动资格start");
		// 先判断过期时间
		if (System.currentTimeMillis() < Long.parseLong(activeDate) * 1000
				) {
			throw new AppException(CommonConstant.GALA_VALIDATE_ACTIVE, "活动未开始");
		}
		if(System.currentTimeMillis() > Long.parseLong(expireDate) * 1000){
			throw new AppException(CommonConstant.GALA_VALIDATE_EXPIRE, "活动已过期");
		}
		//判断参加活动次数
		 String times="0";
		try{
		 times=valueOpsCache.get(CommonConstant.GALA_KEY_USER_PREFIX+phone);
//		 if(times==null){
//			 PrintOrderFilter filter=new PrintOrderFilter();
//				filter.setPhone(phone);
//				times=galaPrintService.findGalaAmountByFilter(filter).getData().toString();
//		 }
		}catch(Exception e){
			PrintOrderFilter filter=new PrintOrderFilter();
			filter.setPhone(phone);
			Result<Integer> result=galaPrintService.findGalaAmountByFilter(filter);
			times=result.getData().toString();
		}
		if(times!=null&&Integer.parseInt(times)>=Integer.parseInt(enterableTimes)){
			throw new AppException(CommonConstant.GALA_VALIDATE_NUMBERS, "您已参加过活动");
		}else{
			times="0";
			valueOpsCache.set(CommonConstant.GALA_KEY_USER_PREFIX+phone,times
					,1,
	                TimeUnit.DAYS);
		}
		//判断用户是否存在,不存在则新建
//		Result<User> userinfo = userApi.getUserByPhone(phone);
//		if (userinfo.getData() == null||userinfo.getData().getUid()==null) {
//			User user = new User();
//			user.setPhone(phone);
//			user.setNickname(phone);
//			user.setUname(phone);
//			user.setRegTime((int) (System.currentTimeMillis() / 1000));
//			userinfo = userApi.createUser(user);
//			if (userinfo.getStatus() != 0) {
//				logger.error("创建用户失败", JSONObject.toJSONString(userinfo));
//				throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "创建用户失败");
//			}
//		}
		//生成用户的订单
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setBuyerId(phone);
//		orderDTO.setBuyerId(userinfo.getData().getUid().toString());
		orderDTO.setBuyerName(phone);
		orderDTO.setBuyerPhone(phone);
		orderDTO.setPayAmount(CommonConstant.GALA_AMOUNTS);
		orderDTO.setOrderAmount(CommonConstant.GALA_AMOUNTS);
		orderDTO.setStatus(OrderStatusEnum.INIT);
		orderDTO.setCreateTime(new Date());
		orderDTO.setSource(OrderSourceEnum.WAP);
		orderDTO.setSellerId("1");
		orderDTO.setType(BizTypeEnum.SHOP_PRINT);
		List<OrderItemDTO> itemList=new ArrayList<OrderItemDTO>();
		OrderItemDTO item=new OrderItemDTO();
		item.setItemId(itemId);
		item.setAmount(CommonConstant.GALA_AMOUNTS);
		item.setPrice(CommonConstant.GALA_AMOUNTS);
		item.setCreateTime(new Date());
		item.setQuantity(quantity);
		itemList.add(item);
		orderDTO.setItemList(itemList);
		Result<OrderCreateResultInfo> orderInfo=orderCreateFacade.createOrder(orderDTO, new OrderContext());
		if(orderInfo.getStatus()!=0){
			throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR,
					"订单创建失败");
		}
		JSONObject json=new JSONObject();
		json.put("phone", phone);
		if (dormId!=null)
		json.put("dormId", dormId);
//		json.put("uid", userinfo.getData().getUid());
		valueOpsCache.set(CommonConstant.GALA_KEY_ORDER_PREFIX+orderInfo.getData().getOrderDTO().getId(),json.toJSONString()
				,1,
                TimeUnit.DAYS);
		
		return DatagramHelper.getDatagram(orderInfo.getData(),CommonConstant.GLOBAL_STATUS_SUCCESS,
				CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}
	
	public Datagram<?> clientPayResult(String orderId){
		Result<OrderDTO> printOrder = buyerOrderQueryFacade.queryOrder(orderId,null);
		if (printOrder.getData() != null &&printOrder.getData().getStatus() == OrderStatusEnum.FINISHED) {
		}else{
			logger.error("订单结果查询－订单id："+printOrder.getData().getId()+"订单状态:"+printOrder.getData().getStatus());
			throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR,
					"支付失败");
		}
		return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_SUCCESS,
				CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}
	public boolean sendCoupon(String phone){
		try{
			//TODO
			//判断用户是否存在，不存在，新建用户，couponAdd需要添加uid
			Map<String, Object> galaMap = new HashMap<String, Object>();
			galaMap.put("couponType", "1");
			galaMap.put("couponAmounts", 1);
//			CouponAdd couponAdd=new CouponAdd();
//			couponAdd.setActiveDate(Long.parseLong(activeDate));
//			couponAdd.setExpireDate(1465487999L);
//			couponAdd.setPhone(phone);
//			couponAdd.setScope(CouponScope.PRINTER.getScope());
//			couponAdd.setType(CouponType.USER_MONEY_COUPON.getType());
//			couponAdd.setDiscount(1D);//优惠金额1元
//			couponAdd.setThreshold(0d);//0门槛
//			couponAdd.setText("照片优惠券");//优惠券标题
//			galaMap.put("data", couponAdd);
//			pushToGala(galaMap);
			return true;
		}catch(Exception e){
			logger.info("520发放优惠券异常："+e.getMessage());
			return false;
		}
	}
	private void pushToGala(Map<String, Object> param) {
		String pushPcString = JsonUtil.getJsonFromObject(param);
		logger.info("gala_print59活动", pushPcString);
		rabbitTemplate.convertAndSend(queueName, pushPcString);
	}
}
