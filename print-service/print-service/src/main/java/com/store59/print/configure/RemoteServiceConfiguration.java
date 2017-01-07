package com.store59.print.configure;

import com.store59.base.common.api.*;
import com.store59.creditcard.common.remoting.CreditCardPayApi;
import com.store59.dorm.common.api.*;
import com.store59.kylin.rpc.client.utils.ProxyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年5月25日 下午4:25:32
 * @since 1.0
 */
@Configuration
public class RemoteServiceConfiguration {

	@Autowired
	private RemoteServiceNameCfg nameCfg;

	@Bean
	OrderPayAbnormalRecordApi orderPayAbnormalRecordApi() {
		return (OrderPayAbnormalRecordApi) buildRemotingServiceName(nameCfg.getBaseService(), "orderpayabnormalrecord",
				OrderPayAbnormalRecordApi.class);
	}

	@Bean
	DormShopApi dormShopApi() {
		return (DormShopApi) buildRemotingServiceName(nameCfg.getDormService(), "dormshop", DormShopApi.class);
	}

	@Bean
	CreditCardPayApi creditCardPayApi() {
		return (CreditCardPayApi) buildRemotingServiceName(nameCfg.getCreditCardService(), "creditcardpay",
				CreditCardPayApi.class);
	}

	private <T> T buildRemotingServiceName(String serviceName, String serviceExportName, Class clazz) {
		return ProxyBuilder.create().setServiceName(serviceName).setServiceExportName(serviceExportName)
				.setInterfaceClass(clazz).build();
	}

}
