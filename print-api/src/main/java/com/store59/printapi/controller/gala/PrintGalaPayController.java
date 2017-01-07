package com.store59.printapi.controller.gala;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.store59.coupon.enums.CouponScope;
import com.store59.coupon.enums.CouponType;
import com.store59.dto.common.enums.PayTypeEnum;
import com.store59.dto.common.order.OrderStatusEnum;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.SignatureUtils;
import com.store59.kylin.utils.StringUtil;
import com.store59.order.common.service.dto.OrderDTO;
import com.store59.order.common.service.dto.OrderPayDTO;
import com.store59.order.common.service.enums.OrderOperateTypeEnum;
import com.store59.order.common.service.enums.OrderPaySourceEnum;
import com.store59.order.common.service.enums.OrderPayStatusEnum;
import com.store59.order.common.service.enums.OrderPayerTypeEnum;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import com.store59.order.common.service.facade.OrderUpdateFacade;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.remoting.GalaPrintService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.MessageConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.gala.GalaPayParam;
import com.store59.printapi.model.result.Datagram;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年4月19日 上午11:24:59 类说明
 */
@RestController
@RequestMapping("/gala/payment/*")
public class PrintGalaPayController {

	private static Logger logger = LoggerFactory.getLogger(PrintGalaPayController.class);

	@Autowired
	private BuyerOrderQueryFacade buyerOrderQueryFacade;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Value("${gala.print.enterabletimes}")
	private String enterableTimes;

	// 微信返回通知的主机名，返回到我自己的这个类的这个方法
	@Value("${gala.print.pay.notify.url}")
	private String notifyUrl;

	@Value("${zhifu.key}")
	private String zhifuKey;

	private final String queueName = "print-gala";
	
	@Value("${gala.print.expiredate}")
	private String expireDate;
	@Value("${gala.print.activedate}")
	private String activeDate;
	private String couponText="59打印优惠券";
	private final int couponAmounts=4;
	private final Double discount=0.5D;
	
    @Value("${cookie.max.age}")
    private int cookieMaxAge;
    
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private OrderUpdateFacade orderUpdate;
    @Autowired
    private GalaPrintService galaPrintService;
	@Autowired
    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, String> cache;

	/**
	 *  gala活动支付统一入口
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public Object paymentAlipay(@Valid GalaPayParam param, BindingResult rs) {
		logger.debug("接收到得请求参数 orderId：{}", param.getOrderId());
		if (rs.hasErrors()) {
			throw new AppException(CommonConstant.STATUS_REQUEST_DATA_INVALID, MessageConstant.ORDER_ID_PARAM_BLANK);
		}
		Result<OrderDTO> printOrder = buyerOrderQueryFacade.queryOrder(param.getOrderId(), null);
		if (printOrder.getData() == null || printOrder.getStatus() != 0
				|| printOrder.getData().getStatus() != OrderStatusEnum.INIT) {
			throw new AppException(CommonConstant.GLOBAL_STATUS_ERROR, "支付宝：该订单已支付或已取消");
		}

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderId", param.getOrderId().toString());
		paramsMap.put("openId", param.getOpenId());
		paramsMap.put("foodName", "59打印一分钱活动");
		paramsMap.put("money", "0.01");
		paramsMap.put("notifyUrl", notifyUrl);
		paramsMap.put("returnUrl", param.getReturnUrl());
		logger.info("加密参数："+JsonUtil.getJsonFromObject(paramsMap).toString());
		String sign = SignatureUtils.sign(paramsMap, zhifuKey);
		JSONObject data=new JSONObject();
		data.put("notifyUrl", notifyUrl);
		data.put("sign", sign);
		return DatagramHelper.getDatagram(data,CommonConstant.GLOBAL_STATUS_SUCCESS,CommonConstant.GLOBAL_STATUS_SUCCESS_MSG);
	}

	/**
	 * 对应文档中的接收通知 POST 应用的地址/notifyUrl
	 * <p/>
	 * 该方法完成的操作： （1）根据 url 进行签名校验，若校验不通过，返回全局错误码 6； （2）根据请求参数 orderId 查询表
	 * 59_order 对应记录的 orderAmount 字段值， 和请求参数 money 比较，如果不一致，返回全局错误码 4； （3）
	 * 以上两步校验通过以后，根据请求参数修改 orderId 对应的记录的 payType 、tradeNo、paystatus。
	 *
	 * @param request
	 * @param status
	 *            订单状态： 0, 新订单，还未确认或发货
	 * @param msg
	 * @param payType
	 *            支付类型： 0 货到付款，1 支付宝，2 微信支付， alipay是支付宝，wxpay是微信支付，spend是59花
	 * @param sign
	 *            校验码
	 * @param orderId
	 *            订单号
	 * @param money
	 *            订单金额
	 * @param tradeNo
	 *            第三方支付交易号
	 * @param payTime
	 *            付款时间
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	public Object orderNotify(HttpServletRequest request, Integer status, String msg, String payType, String sign,
			String orderId, String money, String tradeNo, Integer payTime) throws UnsupportedEncodingException {
		logger.info("接收到的参数 status : {}", status);
		logger.info("接收到的参数 msg : {}", msg);
		logger.info("接收到的参数 payType : {}", payType);
		logger.info("接收到的参数 orderId : {}", orderId);
		logger.info("接收到的参数 sign : {}", sign);
		logger.info("接收到的参数 money : {}", money);
		logger.info("接收到的参数 tradeNo : {}", tradeNo);
		logger.info("接收到的参数 payTime : {}", payTime);

		// 对 URL 的合法性进行校验
		boolean notify = this.notifyUrl(request, zhifuKey);
		if (notify) {
			//验证money，更新订单信息
			orderUpate(orderId, tradeNo, payType);
		} else {
			throw new AppException(6, "Signature Validation failure");
		}
		//异步发优惠券；添加活动数据;添加radis
		Datagram<?> msgResult=sendMsg(orderId);
		if(msgResult.getStatus()!=CommonConstant.GLOBAL_STATUS_SUCCESS){
			return msgResult;
		}
		logger.info("开始返回结果。");
		Datagram<Map<String, String>> datagram = new Datagram<>();
		datagram.setStatus(CommonConstant.GLOBAL_STATUS_SUCCESS);
		datagram.setMsg(msg);
		if (status == 0) {
			Map<String, String> data = new HashMap<>();
			data.put("result", "success");
			datagram.setData(data);
		}
		logger.info("返回结果完毕:{}", JsonUtil.getJsonFromObject(datagram));
		return datagram;
	}

	private boolean notifyUrl(HttpServletRequest request, String zhifuKey) {
		Set<String> requestSet = request.getParameterMap().keySet();
		List<String> strList = new ArrayList<>();
		strList.addAll(requestSet);
		Collections.sort(strList);
		StringBuilder sb = new StringBuilder();
		String value = null;
		String signValue = null;
		for (int i = 0; i < strList.size(); i++) {
			value = request.getParameter(strList.get(i));
			if ("sign".equals(strList.get(i))) {
				signValue = value;
				continue;
			}
			sb.append(strList.get(i)).append("=").append(value).append("&");
		}
		sb.append(zhifuKey);
		String md5Value = DigestUtils.md5Hex(sb.toString());
		if (signValue.endsWith(md5Value)) {
			return true;
		} else {
			return false;
		}
	}

	private void pushToGala(Map<String, Object> param) {
		String pushPcString = JsonUtil.getJsonFromObject(param);
		logger.info("gala_print59活动", pushPcString);
		rabbitTemplate.convertAndSend(queueName, pushPcString);
	}
	private void orderUpate(String orderId,String tradeNo,String payType) throws AppException{
		logger.info("验证参数通过");
		// GET ORDERINFO
		// 校验 money 是否一致
		Result<OrderDTO> orderInfo = buyerOrderQueryFacade.queryOrder(orderId, null);
		if (orderInfo.getData().getPayAmount() != CommonConstant.GALA_AMOUNTS) {
			throw new AppException(4, "Money Validation failure");
		}
		//准备更新订单状态
		OrderPayDTO updatePrintOrder = new OrderPayDTO();
		updatePrintOrder.setOrderId(orderId);
		updatePrintOrder.setPayerType(OrderPayerTypeEnum.BUYER);
		updatePrintOrder.setAmount(orderInfo.getData().getPayAmount());
		updatePrintOrder.setOutId(tradeNo);
		updatePrintOrder.setStatus(OrderPayStatusEnum.FINISHED);
		updatePrintOrder.setType(PayTypeEnum.WXPAY_APP);
		if ("alipay".equals(payType)) {// 支付宝
			updatePrintOrder.setType(PayTypeEnum.ALIPAY_WAP);
		}
		if ("wxpay".equals(payType)) {// 微信支付
			updatePrintOrder.setType(PayTypeEnum.WXPAY_APP);
		}
		updatePrintOrder.setPayerId(orderInfo.getData().getBuyerId());
		updatePrintOrder.setSource(OrderPaySourceEnum.WAP);
		logger.info("更新订单状态");
		Result<?> rs = orderUpdate.saveOrderPay(orderId,OrderStatusEnum.FINISHED,updatePrintOrder,OrderOperateTypeEnum.BUYER_FINISH);
		if (rs.getStatus() != 0) {
			logger.info("更新订单状态入参："+JSONObject.toJSONString(updatePrintOrder));
			logger.info("更新订单状态回参："+JSONObject.toJSONString(rs));
			throw new AppException(5, "Gala Order Update failer");
		}
	
	}
	private Datagram<?> sendMsg(String orderId){
		Object userInfo=valueOpsCache.get(CommonConstant.GALA_KEY_ORDER_PREFIX + orderId);
		JSONObject json = JSONObject.parseObject(userInfo.toString());
		logger.info("异步发优惠券；添加活动数据;添加radis");
		String phone = json.getString("phone");
		String dormId = null;
		if (json.get("dormId") != null)
			dormId = json.getString("dormId");
//		String uid = json.getString("uid");
		String times = valueOpsCache.get(CommonConstant.GALA_KEY_USER_PREFIX + phone);
		if(StringUtil.isEmpty(times)){
			PrintOrderFilter filter=new PrintOrderFilter();
			filter.setPhone(phone);
			Result<Integer> result=galaPrintService.findGalaAmountByFilter(filter);
			times=result.getData().toString();
			valueOpsCache.set(CommonConstant.GALA_KEY_USER_PREFIX+phone,times
					,1,
	                TimeUnit.DAYS);
		}
		if (times != null && Integer.parseInt(times) >= Integer.parseInt(enterableTimes)) {
			logger.info(phone +"您已参加过活动");
			return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "您已参加过活动");
		} else {
			valueOpsCache.set(CommonConstant.GALA_KEY_USER_PREFIX + phone,
					times == null ? "1" : String.valueOf(Integer.parseInt(times) + 1),1,
			                TimeUnit.DAYS);
			String dormCount = null;
			//设置店长相关参数
			if(dormId!=null){
				dormCount=valueOpsCache.get(CommonConstant.GALA_KEY_DORM_PREFIX + dormId);
				logger.info("dormcount:"+dormCount);
				if(StringUtil.isEmpty(dormCount)){
					PrintOrderFilter pof=new PrintOrderFilter();
					pof.setDormId(Integer.parseInt(dormId));
					Result<Integer> dormInt=galaPrintService.findGalaAmountByFilter(pof);
					if(dormInt.getStatus()==0){
						dormCount=dormInt.getData().toString();
					}else{
						logger.error("获取店长dormCount失败；dormId："+dormId);
					}
				}
				logger.info("dormCount"+dormCount+"dormId:"+dormId);
				valueOpsCache.set(CommonConstant.GALA_KEY_DORM_PREFIX + dormId,
						dormCount == null ? "1" : String.valueOf(Integer.parseInt(dormCount) + 1),cookieMaxAge,
				                TimeUnit.SECONDS);
				valueOpsCache.getOperations().opsForZSet().add("printkey", "printdorm"+dormId, Integer.parseInt(dormCount) + 1);
			}
			//设置优惠券信息
			Map<String, Object> galaMap = new HashMap<String, Object>();
			galaMap.put("couponType", "1");
			galaMap.put("dormId", dormId);
			galaMap.put("couponAmounts", couponAmounts);
//			CouponAdd couponAdd=new CouponAdd();
//    		couponAdd.setActiveDate(Long.parseLong(activeDate));
//    		couponAdd.setExpireDate(1465487999L);
//    		couponAdd.setPhone(phone);
//    		couponAdd.setScope(CouponScope.PRINTER.getScope());
//    		couponAdd.setType(CouponType.USER_MONEY_COUPON.getType());
//    		couponAdd.setDiscount(discount);
//    		couponAdd.setThreshold(0d);
//    		couponAdd.setText(couponText);
////    		couponAdd.setUid(Long.parseLong(uid));
////    		couponAdd.setText("满0元使用");
//    		galaMap.put("data", couponAdd);
			pushToGala(galaMap);
		}
		return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_SUCCESS,"");
	}
	@RequestMapping("test")
	private Object test(@RequestParam(required=true)String orderId){
//		OrderPayDTO updatePrintOrder = new OrderPayDTO();
//		updatePrintOrder.setOrderId("03064679485022146491028");
//		updatePrintOrder.setPayerType(OrderPayerTypeEnum.BUYER);
//		updatePrintOrder.setAmount(1);
//		updatePrintOrder.setOutId("4008072001201604255209361319");
//		updatePrintOrder.setStatus(OrderPayStatusEnum.FINISHED);
//		updatePrintOrder.setType(PayTypeEnum.WXPAY_APP);
//		updatePrintOrder.setPayerId(1461573256111028L);
//		updatePrintOrder.setSource(OrderPaySourceEnum.WAP);
//		logger.info("更新订单状态");
//		Result<?> rs = orderUpdate.saveOrderPay("03064129061558984404793",OrderStatusEnum.FINISHED,updatePrintOrder,OrderOperateTypeEnum.BUYER_FINISH);
//		if (rs.getStatus() != 0) {
//			logger.info("更新订单状态入参："+JSONObject.toJSONString(updatePrintOrder));
//			logger.info("更新订单状态回参："+JSONObject.toJSONString(rs));
//			throw new AppException(5, "Gala Order Update failer");
//		}
//		System.out.println(valueOpsCache.get(CommonConstant.GALA_KEY_USER_PREFIX+"13388600226"));
//		Result<OrderDTO> rsa=buyerOrderQueryFacade.queryOrder(orderId, null);
		
//		return rsa.getData().getType().getCode().toString();
//		Map<String, Object> galaMap = new HashMap<String, Object>();
//		galaMap.put("couponType", "1");
//		pushToGala(galaMap);
		Map<String, Object> galaMap = new HashMap<String, Object>();
		galaMap.put("couponType", "1");
		galaMap.put("dormId", null);
		galaMap.put("couponAmounts", couponAmounts);
//		CouponAdd couponAdd=new CouponAdd();
//		couponAdd.setActiveDate(Long.parseLong(activeDate));
//		couponAdd.setExpireDate(Long.parseLong(expireDate));
//		couponAdd.setPhone("18698553771");
//		couponAdd.setScope(CouponScope.PRINTER.getScope());
//		couponAdd.setType(CouponType.USER_MONEY_COUPON.getType());
//		couponAdd.setDiscount(discount);
//		couponAdd.setThreshold(0d);
//		couponAdd.setText(couponText);
//		couponAdd.setUid(Long.parseLong("1460686126411013"));
//		couponAdd.setText("满0元使用");
//		galaMap.put("data", couponAdd);
//		pushToGala(galaMap);
//		valueOpsCache.get("print_gala_dorm_"+13);
		return null;
	}
}
