package com.store59.printapi.model.param;

import com.store59.printapi.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @version 1.0 2016/01/15
 * @since 1.0
 */
public class SmsPhoneParam {

    @NotBlank(message = MessageConstant.PHONE_NUM_NOT_NULL)
    private String phone;

    @NotBlank(message = MessageConstant.SMS_CODE_NOT_NULL)
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}