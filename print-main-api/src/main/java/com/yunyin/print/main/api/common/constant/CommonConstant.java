package com.yunyin.print.main.api.common.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
public interface CommonConstant {

    // HTTP方法
    byte HTTP_METHOD_GET    = 0; // Get方法
    byte HTTP_METHOD_POST   = 1; // Post方法
    byte HTTP_METHOD_PUT    = 2; // Put方法
    byte HTTP_METHOD_PATCH  = 3; // Patch方法
    byte HTTP_METHOD_DELETE = 4; // Delete方法

    //status
    int    GLOBAL_STATUS_ERROR              = -1;  // 未知错误
    int    GLOBAL_STATUS_SUCCESS            = 0;   // 请求成功
    int    GLOBAL_STATUS_DATA_EXCEPTION     = 2;   // 数据异常,重新加载数据
    int    STATUS_TOKEN_INVALID             = 101; // token无效
    int    STATUS_SESSION_TIMEOUT_INVALID   = 102; // 会话过期
    int    STATUS_SIGN_INVALID              = 103; // 签名数据验证失败
    int    STATUS_REQUEST_DATA_INVALID      = 104; // 请求数据有误
    int    STATUS_REQUEST_JSON_DATA_INVALID = 105; // 请求数据的JSON格式有误
    int    STATUS_REQUEST_FILE_INVALID      = 106; // 上传文件为空
    int    STATUS_SERVICE_REQUEST_EXCEPTION = 110; // 请求服务层异常
    int    STATUS_CONVER_JSON               = 111; // json转换异常
    int    STATUS_MAXFILEUPLOAD             = 113; //上传文件大小超过上限
    int    STATUS_EXCEL_NOT_SUPPORT         = 114; //文件上传不支持excel格式
    int    SATTUS_COUPON_NOT_VALID          = 115; //当前优惠券不可用
    int    STATUS_DOC_CONVERT_ERROR         = 120; //文档转换失败
    //message
    String GLOBAL_STATUS_ERROR_MSG          = "未知错误!";
    String GLOBAL_STATUS_SUCCESS_MSG        = "请求成功!";
    String GLOBAL_STATUS_DATA_EXCEPTION_MSG = "数据异常,请重新加载数据!";
    String MSG_TOKEN_INVALID                = "token无效";
    String MSG_SESSION_TIMEOUT_INVALID      = "会话过期";
    String MSG_SIGN_INVALID                 = "签名数据验证失败";
    String MSG_REQUEST_DATA_INVALID         = "请求数据有误";
    String MSG_REQUEST_JSON_DATA_INVALID    = "请求数据的JSON格式有误";
    String MSG_REQUEST_FILE_NULL            = "上传文件为空";
    String MSG_SERVICE_REQUEST_EXCEPTION    = "服务层接口调用时产生异常";
    String MSG_CONVER_JSON                  = "Json转换异常";
    String MSG_STATUS_MAXFILEUPLOAD         = "上传文件大小超过上限";
    String MSG_STATUS_EXCEL_NOT_SUPPORT     = "文件上传不支持excel格式";
    String MSG_SATTUS_COUPON_NOT_VALID      = "当前优惠券不可用,优惠券编号:";
    String MSG_DOC_CONVERT_ERROR            = "文档转换失败";

    //key
    String KEY_COOKIE_NAME_TOKEN            = "token";            // 存储于Cookie里的TokenId
    String KEY_COOKIE_NAME_SECRET           = "secret_key";       // 存储于Cookie里的SecretKey
    String KEY_COOKIE_NAME_LAST_MODIFY_TIME = "last_modify_time"; // 存储于Cookie里的Cookie最后更新时间
    String KEY_REDIS_TOKEN_PREFIX           = "print_token_";       // 存储于Redis里的Token前缀
}
