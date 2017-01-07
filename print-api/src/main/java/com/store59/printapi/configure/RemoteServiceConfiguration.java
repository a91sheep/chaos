package com.store59.printapi.configure;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.aliyun.oss.OSSClient;
import com.store59.ad.common.api.AdApi;
import com.store59.ad.common.api.AdOrderApi;
import com.store59.ad.common.api.UserWeChatService;
import com.store59.base.common.api.CityApi;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.api.OrderPayAbnormalRecordApi;
import com.store59.base.common.api.ProvinceApi;
import com.store59.base.common.api.SiteApi;
import com.store59.base.common.api.SmsAuthApi;
import com.store59.base.common.api.ZoneApi;
import com.store59.coupon.remoting.CouponService;
import com.store59.dorm.common.api.DormApi;
import com.store59.dorm.common.api.DormAssetsApi;
import com.store59.dorm.common.api.DormEntryShopApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.api.DormShopTimeApi;
import com.store59.kylin.rpc.client.utils.ProxyBuilder;
import com.store59.order.common.service.facade.BuyerOrderQueryFacade;
import com.store59.order.common.service.facade.OrderCreateFacade;
import com.store59.order.common.service.facade.OrderUpdateFacade;
import com.store59.order.common.service.facade.SellerOrderQueryFacade;
import com.store59.print.common.remoting.AdOrderRelationService;
import com.store59.print.common.remoting.AdOrderService;
import com.store59.print.common.remoting.GalaPrintService;
import com.store59.print.common.remoting.OrderItemService;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.print.common.remoting.PrintOrderService;
import com.store59.printapi.common.converter.HttpConverter;
import com.store59.stock.common.remoting.DormWarehouseService;
import com.store59.stock.common.remoting.WarehouseService;
import com.store59.user.common.api.UserApi;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年6月16日 下午12:02:24 类说明
 */
@Configuration
public class RemoteServiceConfiguration {

	@Autowired
	private RemoteServiceNameCfg nameCfg;
	// @Autowired
	// private RemoteServiceUrlCfg urlCfg;

	@Value("${aliyun.oss.client.endpoint}")
	private String endpoint;
	@Value("${aliyun.oss.client.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.oss.client.accessKeySecret}")
	private String accessKeySecret;

	@Bean
	public UserApi userApi() {
		return (UserApi) buildRemotingServiceName(nameCfg.getUserService(), "user", UserApi.class);
	}

	@Bean
	public OrderPayAbnormalRecordApi orderPayAbnormalRecordApi() throws MalformedURLException {
		return (OrderPayAbnormalRecordApi) buildRemotingServiceName(nameCfg.getBaseService(), "orderpayabnormalrecord",
				OrderPayAbnormalRecordApi.class);
	}

	@Bean
	public ProvinceApi provinceService() throws MalformedURLException {
		return (ProvinceApi) buildRemotingServiceName(nameCfg.getBaseService(), "province", ProvinceApi.class);
	}

	@Bean
	public CityApi cityService() throws MalformedURLException {
		return (CityApi) buildRemotingServiceName(nameCfg.getBaseService(), "city", CityApi.class);
	}

	@Bean
	public ZoneApi zoneService() throws MalformedURLException {
		return (ZoneApi) buildRemotingServiceName(nameCfg.getBaseService(), "zone", ZoneApi.class);
	}

	@Bean
	public SiteApi siteService() throws MalformedURLException {
		return (SiteApi) buildRemotingServiceName(nameCfg.getBaseService(), "site", SiteApi.class);
	}

	@Bean
	public DormentryApi dormentryService() throws MalformedURLException {
		return (DormentryApi) buildRemotingServiceName(nameCfg.getBaseService(), "dormentry", DormentryApi.class);
	}

	@Bean
	public DormEntryShopApi dormentryShopService() throws MalformedURLException {
		return (DormEntryShopApi) buildRemotingServiceName(nameCfg.getDormService(), "dormentryshop",
				DormEntryShopApi.class);
	}

	@Bean
	public DormShopApi dormeShopService() throws MalformedURLException {
		return (DormShopApi) buildRemotingServiceName(nameCfg.getDormService(), "dormshop", DormShopApi.class);
	}

	@Bean
	public PrintOrderService printOrderService() throws MalformedURLException {
		return (PrintOrderService) buildRemotingServiceName(nameCfg.getPrintService(), "printorder",
				PrintOrderService.class);
	}

	@Bean
	public PrintOrderRecordService printOrderRecordService() throws MalformedURLException {
		return (PrintOrderRecordService) buildRemotingServiceName(nameCfg.getPrintService(), "printorderrecord",
				PrintOrderRecordService.class);
	}

	@Bean
	public DormApi dormApi() throws MalformedURLException {
		return (DormApi) buildRemotingServiceName(nameCfg.getDormService(), "dorm", DormApi.class);
	}

	@Bean
	public SmsAuthApi smsAuthApi() throws MalformedURLException {
		return (SmsAuthApi) buildRemotingServiceName(nameCfg.getBaseService(), "smsauth", SmsAuthApi.class);
	}

	@Bean
	public DormShopDeliveryApi dormShopDeliveryApi() throws MalformedURLException {
		return (DormShopDeliveryApi) buildRemotingServiceName(nameCfg.getDormService(), "dormshopdelivery",
				DormShopDeliveryApi.class);
	}

	@Bean
	public DormShopPriceApi dormShopPriceApi() throws MalformedURLException {
		return (DormShopPriceApi) buildRemotingServiceName(nameCfg.getDormService(), "dormshopprice",
				DormShopPriceApi.class);
	}

	@Bean
	public DormShopTimeApi dormShopTimeApi() throws MalformedURLException {
		return (DormShopTimeApi) buildRemotingServiceName(nameCfg.getDormService(), "dormshoptime",
				DormShopTimeApi.class);
	}

	@Bean
	public CouponService couponService() throws MalformedURLException {
		return (CouponService) buildRemotingServiceName(nameCfg.getCouponService(), "coupon", CouponService.class);
	}

	@Bean
	public AdApi adApi() throws MalformedURLException {
		return (AdApi) buildRemotingServiceName(nameCfg.getAdService(), "ad", AdApi.class);
	}

	@Bean
	public AdOrderApi adOrderApi() throws MalformedURLException {
		return (AdOrderApi) buildRemotingServiceName(nameCfg.getAdService(), "adorder", AdOrderApi.class);
	}

	@Bean
	public GalaPrintService printGalaServiceService() throws MalformedURLException {
		return (GalaPrintService) buildRemotingServiceName(nameCfg.getPrintService(), "printgala",
				GalaPrintService.class);
	}

	@Bean
	public OrderCreateFacade orderCreateFacade() throws MalformedURLException {
		return (OrderCreateFacade) buildRemotingServiceName(nameCfg.getOrderService(), "createOrder",
				OrderCreateFacade.class);
	}

	@Bean
	public OrderUpdateFacade orderUpdateFacade() throws MalformedURLException {
		return (OrderUpdateFacade) buildRemotingServiceName(nameCfg.getOrderService(), "updateOrder",
				OrderUpdateFacade.class);
	}

	@Bean
	public BuyerOrderQueryFacade buyerOrderQueryFacade() throws MalformedURLException {
		return (BuyerOrderQueryFacade) buildRemotingServiceName(nameCfg.getOrderService(), "queryBuyerOrder",
				BuyerOrderQueryFacade.class);
	}

	@Bean
	public SellerOrderQueryFacade sellerOrderQueryFacade() throws MalformedURLException {
		return (SellerOrderQueryFacade) buildRemotingServiceName(nameCfg.getOrderService(), "querySellerOrder",
				SellerOrderQueryFacade.class);
	}

	@Bean
	public UserWeChatService userWechatService() throws MalformedURLException {
		return (UserWeChatService) buildRemotingServiceName(nameCfg.getAdService(), "userwechat",
				UserWeChatService.class);
	}

	@Bean
	public DormWarehouseService dormWarehouseService() throws MalformedURLException {
		return (DormWarehouseService) buildRemotingServiceName(nameCfg.getStockService(), "dormwarehouse",
				DormWarehouseService.class);
	}

	@Bean
	public WarehouseService warehouseService() throws MalformedURLException {
		return (WarehouseService) buildRemotingServiceName(nameCfg.getStockService(), "warehouse",
				WarehouseService.class);
	}

	@Bean
	public DormAssetsApi dormAssetsApi() {
		return (DormAssetsApi) buildRemotingServiceName(nameCfg.getDormService(), "dormassets", DormAssetsApi.class);
	}
	
	@Bean
	public AdOrderRelationService adOrderRelationService() throws MalformedURLException {
		return (AdOrderRelationService) buildRemotingServiceName(nameCfg.getPrintService(), "adorderrelation",
				AdOrderRelationService.class);
	}

	@Bean
	public AdOrderService adOrderService() throws MalformedURLException {
		return (AdOrderService) buildRemotingServiceName(nameCfg.getPrintService(), "adorder", AdOrderService.class);
	}
	
	@Bean
	public OrderItemService printOrderItemService() throws MalformedURLException {
		return (OrderItemService) buildRemotingServiceName(nameCfg.getPrintService(), "orderitem",
				OrderItemService.class);
	}
	// private <T> T buildRemotingService(String serviceUrl, String
	// serviceExportName, Class clazz) {
	// return
	// ProxyBuilder.create().setServiceUrl(serviceUrl).setServiceExportName(serviceExportName)
	// .setInterfaceClass(clazz).build();
	// }

	private <T> T buildRemotingServiceName(String serviceName, String serviceExportName, Class clazz) {
		return ProxyBuilder.create().setServiceName(serviceName).setServiceExportName(serviceExportName)
				.setInterfaceClass(clazz).build();
	}

	/**
	 * Json返回处理器配置
	 *
	 * @param jsonHttpMessageConverter
	 * @return
	 */
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpConverter jsonHttpMessageConverter) {
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		List<HttpMessageConverter<?>> messageConverters = new LinkedList<HttpMessageConverter<?>>();
		messageConverters.add(jsonHttpMessageConverter);
		adapter.setMessageConverters(messageConverters);
		return adapter;
	}

	@Bean
	public HttpConverter jsonHttpMessageConverter() {
		HttpConverter converter = new HttpConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("application", "json"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
		return converter;
	}

	/**
	 * OSS
	 */
	@Bean
	public OSSClient ossClient() {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return ossClient;
	}
}
