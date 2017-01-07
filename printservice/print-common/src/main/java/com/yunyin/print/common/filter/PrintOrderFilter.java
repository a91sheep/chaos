package com.yunyin.print.common.filter;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
public class PrintOrderFilter {
    private List<Byte>    statusList;         // 订单状态 1、已下单  2、订单确认<转码完成>  3、已完成<已支付已打印>  4、已取消
    private Long          uid;            // 用户id
    private List<Integer> payTypes;        //支付类型
    private Integer       siteId;         // 区域（学校）id
    private Long          startTime;       // 下单开始时间
    private Long          endTime;         // 下单结束时间
    private Integer       offset;   //起始页数
    private Integer       limit;    //每页条数

    public List<Byte> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Byte> statusList) {
        this.statusList = statusList;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<Integer> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<Integer> payTypes) {
        this.payTypes = payTypes;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
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
