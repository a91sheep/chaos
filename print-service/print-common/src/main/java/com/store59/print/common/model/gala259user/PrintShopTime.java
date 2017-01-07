/**
 * 
 */
package com.store59.print.common.model.gala259user;

import java.io.Serializable;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月22日 上午11:49:46
 * @since 1.0
 */
@SuppressWarnings("serial")
public class PrintShopTime implements Serializable{
	private Integer	id;		
	private Integer dormId;	//店长id
	private Long 	day;	//打印店营业日期时间戳
	private Double  openHours;//打印店该天营业小时数
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDormId() {
		return dormId;
	}
	public void setDormId(Integer dormId) {
		this.dormId = dormId;
	}
	public Long getDay() {
		return day;
	}
	public void setDay(Long day) {
		this.day = day;
	}
	public Double getOpenHours() {
		return openHours;
	}
	public void setOpenHours(Double openHours) {
		this.openHours = openHours;
	}
	
	
}
