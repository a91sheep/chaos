package com.store59.printapi.common.wechat;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.wechat.api.TokenAPI;
import com.store59.printapi.model.param.wechat.token.Token;

@Component
public class TokenManager {

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    @Value("${print.wechat.appid}")
    private String appid;
    @Value("${print.wechat.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    private TokenManager() {

    }

    /**
     * 初始化token 刷新，每118分钟刷新一次。
     */
    @Scheduled(fixedRate = 118 * 60 * 1000)
    public void init() {
        try {
            if (valueOpsCache.setIfAbsent(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX,
                    String.valueOf(System.currentTimeMillis()))) {
                Token token = TokenAPI.token(appid, secret);
                if (token.getAccess_token() != null)
                    valueOpsCache.set(CommonConstant.WECHAT_TOKEN_PREFIX + appid, token.getAccess_token(), 7200,
                            TimeUnit.SECONDS);
                logger.info("ACCESS_TOKEN refurbish with appid:{}", appid, ";token:" + token.getAccess_token());
                valueOpsCache.getOperations().delete(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX);
            }
        } catch (Exception e) {
            logger.error("获取微信token异常：" + e.getMessage());
        }
    }

    /**
     * 获取 access_token 目前认为只有一个appid
     *
     * @param appid
     * @return
     */
    public String getToken(String appid) {
        if (StringUtil.isBlank(valueOpsCache.get(CommonConstant.WECHAT_TOKEN_PREFIX + this.appid))) {
            refurbish();
        }
        return valueOpsCache.get(CommonConstant.WECHAT_TOKEN_PREFIX + this.appid);
    }

    /**
     * 手工刷新
     */
    public void refurbish() {
        try {
            if (valueOpsCache.setIfAbsent(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX,
                    String.valueOf(System.currentTimeMillis())) || !StringUtil.isBlank(valueOpsCache.get(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX))) {
                Token token = TokenAPI.token(appid, secret);
                if (token.getAccess_token() != null)
                    valueOpsCache.set(CommonConstant.WECHAT_TOKEN_PREFIX + appid, token.getAccess_token(), 7200,
                            TimeUnit.SECONDS);
                logger.info("ACCESS_TOKEN refurbish manual with appid:{}", appid, ";token:" + token.getAccess_token());
                valueOpsCache.getOperations().delete(CommonConstant.WECHAT_TOKEN_LOCK_PREFIX);
            }
        } catch (Exception e) {
            logger.error("获取微信token异常：" + e.getMessage());
        }
    }
}
