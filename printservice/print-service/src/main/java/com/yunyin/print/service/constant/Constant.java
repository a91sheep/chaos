package com.yunyin.print.service.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
public interface Constant {
    int EXCEPTION_CODE_NULL = 1;  //必填字段为空的异常

    //业务异常code
    int EXCEPTION_CODE_ORDER_STATUS_ILLEGAL = 1000;  //订单状态非法
    int EXCEPTION_CODE_ORDER_CANCEL_ILLEGAL = 1001;  //订单取消异常
}
