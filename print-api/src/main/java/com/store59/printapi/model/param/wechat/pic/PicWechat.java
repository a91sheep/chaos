package com.store59.printapi.model.param.wechat.pic;

import java.util.List;

import com.store59.printapi.model.param.wechat.message.EventMessage;

/**
 * radis数据model类
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月12日 下午7:28:02 
 * 类说明
 */
public class PicWechat {
	List<EventMessage> list;
	int timestamp;
	public List<EventMessage> getList() {
		return list;
	}
	public void setList(List<EventMessage> list) {
		this.list = list;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
}
