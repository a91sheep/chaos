/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.store59.pay.service.wangshang;

import org.apache.commons.codec.binary.Base64;

import cn.topca.api.cert.CertApiException;
import cn.topca.api.cert.CertSet;
import cn.topca.api.cert.CertStore;
import cn.topca.api.cert.Certificate;

/**
 * 
 * @author simon.xxm
 * @version $Id: Sign.java, v 0.1 2016年1月12日 下午9:14:43 simon.xxm Exp $
 */
public class Sign {

    /**
     * 
     * @author simon.xxm
     * @version $Id: Sign.java, v 0.1 2016年1月13日 下午3:49:42 simon.xxm Exp $
     * @param plainData 原文
     * @param encapsulate 是否包含原文
     */
    public static String sign(String plainData, boolean encapsulate, String confYingqizhilian, String confTopESA) {
        Certificate certificate = null;
        CertSet certSet = null;
        try {
            ConfigTool.getInstance().init(confYingqizhilian, confTopESA);
            certSet = CertStore.listAllCerts();
            certificate = certSet.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //是否包含原文
        byte[] signedData = null;
        try {
            //签名
            signedData = certificate.signP7(plainData.getBytes(), encapsulate);
        } catch (CertApiException e) {
            e.printStackTrace();
        }
        String sign = Base64.encodeBase64String(signedData);
        return sign;
    }

}
