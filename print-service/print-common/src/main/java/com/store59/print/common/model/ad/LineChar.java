package com.store59.print.common.model.ad;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年8月16日 下午2:38:56 类说明
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class LineChar implements Serializable {

	private String datetime;
	private String times;

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

}
