package com.store59.print.common.model.gala;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GalaPrintRelation implements Serializable{
	private Integer  id;        // id
    private Integer  dormId;    //店长id
    private Long     uid;       // 用户id
    private String   phone;     // 用户手机号
    private Integer  createTime; //创建时间
    
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
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Integer getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}  
  
}

