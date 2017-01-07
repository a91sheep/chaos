package com.store59.printapi.common.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
public interface Constant {
    // key for app
    String KEY_REDIS_TOKEN_PREFIX = "token_";
    int STATUS_TOKEN_UID_NULL = 10; // 用户未登录
    String MSG_TOKEN_UID_NULL = "用户未登录";
    int STATUS_TOKEN_INVALID = 2; // token无效
    String MSG_TOKEN_INVALID = "token无效";
}
