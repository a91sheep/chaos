/**
 * 
 */
package com.store59.print.common.model.gala259user;

import java.io.Serializable;

/**
 * 返款计划表
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:39:18
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Print259Repay implements Serializable{
	private Integer id;			
	private Integer dormId;		//店长id
	private Byte 	periods;	//期数
	private Byte 	openStatus;	//营业状态 0-考核未开始、1-正常、2-失效
	private Byte 	repayStatus;//返款状态 0-待返款、1-已返款、2-返款取消
	private Long 	checkTime;	//考核时间
	private Long 	repayTime;	//返款时间
	
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
	public Byte getPeriods() {
		return periods;
	}
	public void setPeriods(Byte periods) {
		this.periods = periods;
	}
	public Byte getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(Byte openStatus) {
		this.openStatus = openStatus;
	}
	public Byte getRepayStatus() {
		return repayStatus;
	}
	public void setRepayStatus(Byte repayStatus) {
		this.repayStatus = repayStatus;
	}
	public Long getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Long checkTime) {
		this.checkTime = checkTime;
	}
	public Long getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(Long repayTime) {
		this.repayTime = repayTime;
	}
	
	
	
	
}
