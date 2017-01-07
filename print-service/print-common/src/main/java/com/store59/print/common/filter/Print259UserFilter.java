/**
 * 
 */
package com.store59.print.common.filter;

import java.io.Serializable;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午2:31:11
 * @since 1.0
 */

@SuppressWarnings("serial")
public class Print259UserFilter implements Serializable{
	private Integer dorm_id;    //店长id
	private Byte    status;		//新用户状态
	private String  nickname;	//用户昵称
	
    private Integer offset;   //起始页数
    private Integer limit;    //每页条数
    
	public Integer getDorm_id() {
		return dorm_id;
	}
	public void setDorm_id(Integer dorm_id) {
		this.dorm_id = dorm_id;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
