/**
 * 
 */
package com.store59.print.common.filter;

import java.io.Serializable;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年8月23日 下午5:17:13
 * @since 1.0
 */
@SuppressWarnings("serial")
public class FreePrintOrderFilter implements Serializable{
	private String orderId;
	private Integer dormId;
	private Byte status; // 订单状态 （0 新订单，未打印，1 已打印）
	private Byte isProfit; // 是否已结算(0: 未结算 1:已结算)
	private String condition; // 查询条件
	private Integer offset; // 起始页数
	private Integer limit; // 每页条数

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getDormId() {
		return dormId;
	}

	public void setDormId(Integer dormId) {
		this.dormId = dormId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getIsProfit() {
		return isProfit;
	}

	public void setIsProfit(Byte isProfit) {
		this.isProfit = isProfit;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
