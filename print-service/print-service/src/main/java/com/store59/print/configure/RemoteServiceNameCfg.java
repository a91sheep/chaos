package com.store59.print.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年5月25日 下午4:23:37
 * @since 1.0
 */
@ConfigurationProperties(prefix = "callService.names")
@Component
public class RemoteServiceNameCfg {
    private String baseService;
    private String dormService;
    private String creditCardService;

    public String getBaseService() {
        return baseService;
    }

    public void setBaseService(String baseService) {
        this.baseService = baseService;
    }

    public String getDormService() {
        return dormService;
    }

    public void setDormService(String dormService) {
        this.dormService = dormService;
    }

	public String getCreditCardService() {
		return creditCardService;
	}

	public void setCreditCardService(String creditCardService) {
		this.creditCardService = creditCardService;
	}
    
    

}
