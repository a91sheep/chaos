/**
 *
 */
package com.store59.print.common.filter;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月29日
 * @since 1.1
 */
@SuppressWarnings("serial")
public class PrintOrderFilter implements Serializable {
    private Integer    dormId;          // 店长id
    private Long       uid;             // 用户id
    private Long       startTime;       // 下单开始时间
    private Long       endTime;         // 下单结束时间
    private List<Integer> statusList;      // 订单状态
    private String     phone;           //电话号码
    private List<Integer> payTypes;        //支付类型
    private Boolean    isPay;           //是否已支付
    private Byte       payType;         //支付类型
    private Long       payTime;         //支付时间
    private Byte       docType;			//文档类型 0照片，1文档
    private String     tradeNo;         //第三方支付交易号

    private Integer offset;   //起始页数
    private Integer limit;    //每页条数

    public Integer getDormId() {
        return dormId;
    }

    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public List<Integer> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<Integer> payTypes) {
        this.payTypes = payTypes;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Byte getDocType() {
		return docType;
	}

	public void setDocType(Byte docType) {
		this.docType = docType;
	}
    

}
