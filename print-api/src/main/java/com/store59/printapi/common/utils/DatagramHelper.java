package com.store59.printapi.common.utils;

import com.store59.printapi.model.result.Datagram;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/15
 * @since 1.0
 */
public class DatagramHelper {
    public DatagramHelper() {
    }

    public static Datagram getDatagram(Object obj, int status, String msg, boolean isApp) {
        Datagram ret = new Datagram();
        ret.setData(obj);
        ret.setMsg(msg);
        ret.setStatus(status);
        ret.setIsApp(isApp);
        return ret;
    }

    public static Datagram getDatagram(Object obj, int status, String msg) {
        Datagram ret = new Datagram();
        ret.setData(obj);
        ret.setMsg(msg);
        ret.setStatus(status);
        ret.setIsApp(false);
        return ret;
    }

    public static Datagram getDatagram(int status, String msg,boolean isApp) {
        Datagram ret = new Datagram();
        ret.setMsg(msg);
        ret.setStatus(status);
        ret.setIsApp(isApp);
        return ret;
    }

    public static Datagram getDatagram(int status, String msg) {
        Datagram ret = new Datagram();
        ret.setMsg(msg);
        ret.setStatus(status);
        ret.setIsApp(false);
        return ret;
    }

    public static Datagram getDatagramWithSuccess(Object obj) {
        return getDatagram(obj, 0, "");
    }

    public static Datagram getDatagramWithSuccess() {
        return getDatagram(0, "");
    }
}


