package com.store59.pay.service.signature;

import com.store59.pay.model.constants.TianyipayConstants;
import com.store59.pay.service.config.TianyipayConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 翼支付签名
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 21:11
 */
@Component
public class TianyipaySignature extends DefaultSignature {
    private static final Logger logger = LoggerFactory.getLogger(TianyipaySignature.class);

    @Autowired
    private TianyipayConfig tianyipayConfig;

    /**
     * 获取签名用的密钥.
     *
     * @return 密钥
     */
    protected String getSignKey() {
        return tianyipayConfig.getKey();
    }

    /**
     * 签名
     * @param parameters
     * @return
     */
    @Override
    public String sign(Map<String, String> parameters) {
        if (MapUtils.isEmpty(parameters) || StringUtils.isBlank(getSignKey())) {
            return "";
        }

        //按照翼支付要求排序
        parameters = sortSignMap(parameters);

        //转换成url格式
        String url = com.store59.pay.util.lang.MapUtils.toUrl(parameters);

        url += "&" + TianyipayConstants.KEY + "=" + getSignKey();
        logger.debug("【翼支付签名】对这个字符串加密={}", url);
        logger.debug("【翼支付签名】加密key={}", getSignKey());

        String result = DigestUtils.md5Hex(url);
        logger.debug("【翼支付签名】加密后的结果, result={}", result);

        //转换为大写
        result = StringUtils.upperCase(result);
        logger.debug("【翼支付签名】转换为大写, result={}", result);

        return result;
    }

    /**
     * 对参数进行排序
     * 坑爹的翼支付, 签名时字段顺序必须按照他给的, 无任何规律可言
     * @param parameters
     * @return
     */
    private Map<String, String> sortSignMap(Map<String, String> parameters){
        Map<String, String> map = new LinkedHashMap<>();

        map.put("UPTRANSEQ", parameters.get("UPTRANSEQ"));
        map.put("MERCHANTID", parameters.get("MERCHANTID"));
        map.put("ORDERSEQ", parameters.get("ORDERSEQ"));
        map.put("ORDERAMOUNT", parameters.get("ORDERAMOUNT"));
        map.put("RETNCODE", parameters.get("RETNCODE"));
        map.put("RETNINFO", parameters.get("RETNINFO"));
        map.put("TRANDATE", parameters.get("TRANDATE"));

        return map;
    }
}
