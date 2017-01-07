package com.store59.printapi.model.param;

import com.store59.printapi.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class LoginParams {

    @NotBlank(message = MessageConstant.LOGIN_CALLBACK_PARAMS_BLANK)
    private String smartTicket;
    private Integer type=1;//0:图片，1:文档

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSmartTicket() {
        return smartTicket;
    }

    public void setSmartTicket(String smartTicket) {
        this.smartTicket = smartTicket;
    }
}