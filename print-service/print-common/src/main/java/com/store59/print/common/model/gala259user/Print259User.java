/**
 * 
 */
package com.store59.print.common.model.gala259user;

import java.io.Serializable;

/**
 * 259新用户
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午2:17:52
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Print259User implements Serializable {
	private Integer id;
	private Integer dormId;		//店长id
	private Long    uid;    	//用户id
	private String  nickname;	//用户昵称
	private Byte    status;  	//新用户状态（0-全部、1-已完成、2-进行中、3-失败）
	private Long    updateTime; //新用户状态更新时间
	
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
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
}
