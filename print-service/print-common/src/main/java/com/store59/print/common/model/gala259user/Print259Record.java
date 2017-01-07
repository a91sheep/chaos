/**
 * 
 */
package com.store59.print.common.model.gala259user;

import java.io.Serializable;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午6:12:44
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Print259Record implements Serializable{
	private Integer id;
	private Integer dormId;			//店长id
	private Long 	finish259Time;	//店长完成259新用户的时间
	private Byte 	isValid;		//店长返款状态 0-失效或全部返款完成、1-正常
	
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
	public Long getFinish259Time() {
		return finish259Time;
	}
	public void setFinish259Time(Long finish259Time) {
		this.finish259Time = finish259Time;
	}
	public Byte getIsValid() {
		return isValid;
	}
	public void setIsValid(Byte isValid) {
		this.isValid = isValid;
	} 
	
	
}
