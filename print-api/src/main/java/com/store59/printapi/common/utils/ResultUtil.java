/**
 * 
 */
package com.store59.printapi.common.utils;

import com.store59.kylin.common.model.Result;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.enums.RetCodeEnum;
import com.store59.printapi.model.result.Datagram;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
public class ResultUtil {

    public static void setResult(Result<?> result, RetCodeEnum retCodeEnum) {
        result.setStatus(retCodeEnum.getStatus());
        result.setMsg(retCodeEnum.getMsg());
    }

    public static <T> Datagram<T> getResult(RetCodeEnum retCodeEnum) {
        Datagram<T> result = new Datagram<>();
        result.setMsg(retCodeEnum.getMsg());
        result.setStatus(retCodeEnum.getStatus());
        return result;
    }

    public static <T> Datagram<T> getResult(int status, String msg) {
        Datagram<T> result = new Datagram<>();
        result.setMsg(msg);
        result.setStatus(status);
        return result;
    }

    public static <T> Datagram<T> getResult(T data, RetCodeEnum retCodeEnum) {
        Datagram<T> result = new Datagram<>();
        result.setData(data);
        result.setMsg(retCodeEnum.getMsg());
        result.setStatus(retCodeEnum.getStatus());
        return result;
    }

    public static <T> Datagram<T> getSuccessResult(T data) {
        return getResult(data, RetCodeEnum.SUCCESS);
    }

    public static <T> Datagram<T> getSuccessResult() {
        return getResult(RetCodeEnum.SUCCESS);
    }
}
