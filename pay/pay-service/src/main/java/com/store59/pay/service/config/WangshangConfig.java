/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 网商银行配置
 * @author beangou
 *
 */
@Component
@ConfigurationProperties(prefix = "wangshang")
public class WangshangConfig {
	private String confTopESA;
	private String confYinqizhilian;
    // FTP服务器用户名	
	private String sftpUserName;
    // FTP服务器密码
    private String sftpPassword;
    // FTP服务器IP地址
    private String sftpIp;
    
    // 端口号
    private int sftpPort;
    private String appId;
    private String partner;
    // 接口地址
	private String reqUrl;
	// 银行卡号
	private String cardNo;
	//会员id
	private String accountId;
	//网商名册文件上传地址
	private String uploadPath;
	//网商结果文件下载地址
	private String downloadPath;
	//短信接口
	private String smscodeFunction;
	//批量代发确认接口
	private String confirmFunction;
	//ERP接口地址
	private String erpReqUrl;
	
    /**
	 * @return the smscodeFunction
	 */
	public String getSmscodeFunction() {
		return smscodeFunction;
	}
	/**
	 * @param smscodeFunction the smscodeFunction to set
	 */
	public void setSmscodeFunction(String smscodeFunction) {
		this.smscodeFunction = smscodeFunction;
	}
	/**
	 * @return the confirmFunction
	 */
	public String getConfirmFunction() {
		return confirmFunction;
	}
	/**
	 * @param confirmFunction the confirmFunction to set
	 */
	public void setConfirmFunction(String confirmFunction) {
		this.confirmFunction = confirmFunction;
	}
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the uploadPath
	 */
	public String getUploadPath() {
		return uploadPath;
	}
	/**
	 * @param uploadPath the uploadPath to set
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	/**
	 * @return the downloadPath
	 */
	public String getDownloadPath() {
		return downloadPath;
	}
	/**
	 * @param downloadPath the downloadPath to set
	 */
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @return the partner
	 */
	public String getPartner() {
		return partner;
	}
	/**
	 * @param partner the partner to set
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}
	/**
	 * @return the reqUrl
	 */
	public String getReqUrl() {
		return reqUrl;
	}
	/**
	 * @param reqUrl the reqUrl to set
	 */
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	/**
	 * @return the sftpUserName
	 */
	public String getSftpUserName() {
		return sftpUserName;
	}
	/**
	 * @param sftpUserName the sftpUserName to set
	 */
	public void setSftpUserName(String sftpUserName) {
		this.sftpUserName = sftpUserName;
	}
	/**
	 * @return the sftpPassword
	 */
	public String getSftpPassword() {
		return sftpPassword;
	}
	/**
	 * @param sftpPassword the sftpPassword to set
	 */
	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}
	/**
	 * @return the sftpIp
	 */
	public String getSftpIp() {
		return sftpIp;
	}
	/**
	 * @param sftpIp the sftpIp to set
	 */
	public void setSftpIp(String sftpIp) {
		this.sftpIp = sftpIp;
	}
	/**
	 * @return the sftpPort
	 */
	public int getSftpPort() {
		return sftpPort;
	}
	/**
	 * @param sftpPort the sftpPort to set
	 */
	public void setSftpPort(int sftpPort) {
		this.sftpPort = sftpPort;
	}
	/**
	 * @return the confYinqizhilian
	 */
	public String getConfYinqizhilian() {
		return confYinqizhilian;
	}
	/**
	 * @param confYinqizhilian the confYinqizhilian to set
	 */
	public void setConfYinqizhilian(String confYinqizhilian) {
		this.confYinqizhilian = confYinqizhilian;
	}
	/**
	 * @return the confTopESA
	 */
	public String getConfTopESA() {
		return confTopESA;
	}
	/**
	 * @param confTopESA the confTopESA to set
	 */
	public void setConfTopESA(String confTopESA) {
		this.confTopESA = confTopESA;
	}
	/**
	 * @return the erpReqUrl
	 */
	public String getErpReqUrl() {
		return erpReqUrl;
	}
	/**
	 * @param erpReqUrl the erpReqUrl to set
	 */
	public void setErpReqUrl(String erpReqUrl) {
		this.erpReqUrl = erpReqUrl;
	}
	
}
