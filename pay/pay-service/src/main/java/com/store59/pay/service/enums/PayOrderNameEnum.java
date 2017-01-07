/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.service.enums;

import com.store59.kylin.utils.EnumCode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * 发起支付的时候显示的订单名字,例如59store夜猫店订单
 *
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 2016年08月08日
 * @since 1.0
 */
public enum PayOrderNameEnum implements EnumCode<Integer> {

    /** 上传文件中. */
    UPLOADING(0, "上传文件中"),

    /** 成功上传文件. */
    UPLOADED(1, "成功上传文件"),

    /** 确认批量代发成功. */
    CONFIRMED_SUCCESS(2, "确认批量代发成功"),

    /** 确认批量代发失败. */
    CONFIRMED_FAILURE(3, "确认批量代发失败"),

    /** 成功下载结果文件. */
    DOWNLOAD_RESULT_FILE_SUCCESS(4, "下载结果文件成功"),

    /** 下载结果文件失败. */
    DOWNLOAD_RESULT_FILE_FAILURE(5, "下载结果文件失败"),

    /** 通知ERP结果成功. */
    NOTICING_ERP_RESULT_SUCCESS(6, "通知ERP结果成功"),

    /** 通知ERP结果失败. */
    NOTICING_ERP_RESULT_FAILURE(7, "通知ERP结果失败"),
    ;

    /** 枚举码. */
    private int    code;

    /** 枚举描述. */
    private String msg;

    /**
     * 构造方法.
     *
     * @param code 枚举码
     * @param msg 枚举描述
     */
    PayOrderNameEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @see EnumCode#getCode()
     */
    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * 格式化枚举描述信息.
     * 
     * @return the formatArgs 格式化参数
     */
    public String getMsg(Object... formatArgs) {
        if (ArrayUtils.isEmpty(formatArgs) || StringUtils.isBlank(msg)) {
            return msg;
        }

        return MessageFormat.format(msg, formatArgs);
    }

    public String getMsg() {
        return msg;
    }

}
