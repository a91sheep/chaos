package com.store59.printapi.model.param.wechat;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月13日 下午3:12:33 
 * 类说明
 */
public class WechatBindingParams {

    private String smartTicket;
    
    private String openid;
    
    public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSmartTicket() {
        return smartTicket;
    }

    public void setSmartTicket(String smartTicket) {
        this.smartTicket = smartTicket;
    }
}