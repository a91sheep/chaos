/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.util;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.pay.model.enums.BizResultCodeEnum;

/**
 * service返回result工具类
 * @author <a href="mailto:lly835@163.com">凌云</a>
 * @version 1.0 16/8/4
 * @since 1.0
 */
public class ServiceResultUtil {
    public static final Result toResult(BizResultCodeEnum resultCodeEnum) {
        return ResultHelper.genResult(resultCodeEnum.getCode(), resultCodeEnum.getDesc());
    }
}
