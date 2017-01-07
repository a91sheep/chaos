package com.store59.base.service;

import com.store59.base.BaseCount;
import com.store59.base.common.api.SmsAuthApi;
import com.store59.base.mq.Sender;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.JsonUtil;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = SmsAuthApi.class, exportPath = "/smsauth")
public class SmsAuthService implements SmsAuthApi {

    @Autowired
    private Sender sender;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    public Result<String> sendAuthCode(String authType, String phone,
                                       Integer timeout) {
        String cacheKey = String.format("%s%s%s", BaseCount.CACHE_PREFIX, authType, phone);
        String authCode = valueOpsCache.get(cacheKey);
        if(authCode == null){
            authCode = generateCode();
        }

        if (timeout == null || timeout < 1) {
            // default
            timeout = 300;
        }
        valueOpsCache.set(cacheKey, authCode, timeout, TimeUnit.SECONDS);
        // send authCode
        Map<String, Object> event = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("key", "smstemp");
        params.put("type", authType);
        params.put("phone", phone);
        params.put("v_code", authCode);
        event.put("target", BaseCount.SMS_AUTH_TARGET);
        event.put("param", params);
        String eventString = JsonUtil.getJsonFromObject(event);
//        rabbitTemplate.convertAndSend(entryName, eventString);

        sender.sendMessage(eventString);

        return ResultHelper.genResultWithSuccess(authCode);
    }

    public Result<Boolean> checkAuthCode(String authType, String phone,
            String authCode) {
        Result<Boolean> result = new Result<>();
        result.setData(false);

        String cacheKey = String.format("%s%s%s", BaseCount.CACHE_PREFIX, authType, phone);
        String code = valueOpsCache.get(cacheKey);
        if(code != null){
            if(code.equals(authCode)) {
                result.setData(true);
                valueOpsCache.getOperations().delete(cacheKey);
            }
        }

        return result;
    }

    private String generateCode() {
        Random random = new Random();
        int bound = 1_000_000;
        int codeValue = random.nextInt(bound);
        String authCode = String.format("%06d", codeValue);
        return authCode;
    }

}
