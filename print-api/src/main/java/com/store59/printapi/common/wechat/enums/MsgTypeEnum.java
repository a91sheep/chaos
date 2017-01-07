package com.store59.printapi.common.wechat.enums;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月11日 下午4:40:07 
 * 微信消息类型
 */
public enum MsgTypeEnum {

	TEXT("text"), IMAGE("image"),VOICE("voice"),VIDEO("video"),LOCATION("location"),LINK("link"),EVENT("event");

	private String name;

	MsgTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public static MsgTypeEnum MsgTOMsg(String type){
		return valueOf(type);
	}

}