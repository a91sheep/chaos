package com.store59.printapi.common.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
//常量和对应信息
public interface CommonConstant {

    //status
    int GLOBAL_STATUS_ERROR              = -1;  // 未知错误
    int GLOBAL_STATUS_SUCCESS            = 0;   // 请求成功
    int GLOBAL_STATUS_DATA_EXCEPTION     = 2;   // 数据异常,重新加载数据
    int STATUS_TOKEN_INVALID             = 101; // token无效
    int STATUS_SESSION_TIMEOUT_INVALID   = 102; // 会话过期
    int STATUS_SIGN_INVALID              = 103; // 签名数据验证失败
    int STATUS_REQUEST_DATA_INVALID      = 104; // 请求数据有误
    int STATUS_REQUEST_JSON_DATA_INVALID = 105; // 请求数据的JSON格式有误
    int STATUS_REQUEST_FILE_INVALID      = 106; // 上传文件为空
    int STATUS_SERVER_TIME_NOTSYNC       = 107; // 服务器时间未同步（请同步服务器时间）
    int STATUS_ACCESS_PATH_ILLEGAL       = 108; // 非法访问
    int STATUS_SERVICE_REQUEST_EXCEPTION = 110; // 请求服务层异常
    int STATUS_CONVER_JSON               = 111; // json转换异常
    int STATUS_DATA_BLANK                = 112; //请求数据为空
    int STATUS_MAXFILEUPLOAD             = 113; //上传文件大小超过上限
    int STATUS_EXCEL_NOT_SUPPORT         = 114; //文件上传不支持excel格式
    int SATTUS_COUPON_NOT_VALID          = 115; //当前优惠券不可用
    int STATUS_DORMSHOP_NOT_WORKING      = 116; //店铺处于非营业状态.
    int STATUS_FREE_PRINT_UNABLE         = 117;  //不能使用免费打印
    int STATUS_SHOPPRINCE_ISBLANK        = 118;  //店铺打印价格为空
    int STATUS_H5_CART_LIST				 = 119;	//购物车列表为空
    int STATUS_DOC_CONVERT_ERROR		 = 120; //文档转换失败
    int STATUS_DORM_CLOSED_ERROR		 = 121; //文档转换失败
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
    String MSG_SERVER_TIME_NOTSYNC          = "请同步服务器时间";
    String MSG_ACCESS_PATH_ILLEGAL          = "非法访问";
    String MSG_SERVICE_REQUEST_EXCEPTION    = "服务层接口调用时产生异常";
    String MSG_CONVER_JSON                  = "Json转换异常";
    String MSG_STATUS_MAXFILEUPLOAD         = "上传文件大小超过上限";
    String MSG_STATUS_EXCEL_NOT_SUPPORT     = "文件上传不支持excel格式";
    String MSG_SATTUS_COUPON_NOT_VALID      = "当前优惠券不可用,优惠券编号:";
    String MSG_STATUS_FREE_PRINT_UNABLE     = "免费资源已被抢光啦～";
    String MSG_H5_CART_LIST					= "抱歉，您上传的照片已失效，请重新上传";
    String MSG_H5_OPENID_NOT_FOUND			= "openid没有找到";
    String MSG_DOC_CONVERT_ERROR			= "文档转换失败";
    String MSG_DORM_CLOSED_ERROR			= "店铺休息中，请稍后再试";
    int LOGIN_SMS_TYPE_REGISTER  = 1;  //注册账户并登录
    int LOGIN_SMS_TYPE_AUTOLOGIN = 2;  //已有账户自动登录

    // HTTP方法
    byte HTTP_METHOD_GET    = 0; // Get方法
    byte HTTP_METHOD_POST   = 1; // Post方法
    byte HTTP_METHOD_PUT    = 2; // Put方法
    byte HTTP_METHOD_PATCH  = 3; // Patch方法
    byte HTTP_METHOD_DELETE = 4; // Delete方法

    // Key
    String KEY_COOKIE_NAME_TYPE             = "type";
    String KEY_COOKIE_NAME_TOKEN            = "token";            // 存储于Cookie里的TokenId
    String KEY_COOKIE_NAME_SECRET           = "secret_key";       // 存储于Cookie里的SecretKey
    String KEY_COOKIE_NAME_LAST_MODIFY_TIME = "last_modify_time"; // 存储于Cookie里的Cookie最后更新时间
    String KEY_REDIS_TOKEN_PREFIX           = "print_token_";       // 存储于Redis里的Token前缀
    String KEY_REDIS_USER_PREFIX            = "print_user_";      //缓存登录用户信息前缀
    String VALUE_COOKIE_NOTINVALID          = "0";
    String VALUE_COOKIE_LOGINEXCEPTION      = "1";
    String VALUE_COOKIE_LOGOUTEXCEPTION     = "2";

    String KEY_REDIS_PROVINCE_PREFIX            = "print_province_";  //缓存省份前缀
    String KEY_REDIS_CITY_PREFIX                = "print_province_city_"; //缓存市前缀
    String KEY_REDIS_ZONE_PREFIX                = "print_province_city_zone_";  //缓存区前缀
    String KEY_REDIS_SITE_PREFIX                = "print_province_city_zone_site_";  //缓存学校前缀
    String KEY_REDIS_SITE_BY_CITYID_PREFIX      = "print_site_by_cityid_";  //根据城市id查询学校
    String KEY_REDIS_SHOP                       = "print_shop_";            //缓存店铺前缀
    String KEY_REDIS_SHOP_BY_DORMENTRYID        = "print_shop_by_dormentryid_"; //缓存楼栋下的店铺
    String KEY_REDIS_DROMENTRYS_BY_SITEID       = "print_dormentry_by_siteid_"; //缓存楼栋
    String KEY_REDIS_DROMENTRYS_SHOPS_BY_SITEID = "print_dormentrys_shops_by_siteid_"; //缓存楼栋和店铺
    String KEY_REDIS_DORMSHOPS_BY_SITEID        = "print_dormshops_by_siteid_";  //学校获取所有楼栋店铺
    String KEY_REDIS_DORM       				= "print_dorm_";//缓存dorm信息
    String KEY_REDIS_DORM_SHOP					= "print_shop_by_dormid_";
    String KEY_REDIS_DORM_DELIVERY				= "print_dorm_delivery_";
    String PRINT_OPTYMIZE_SHOP					="print_opt_shop_";
    String PRINT_OPTYMIZE_DROMENTRYS			="print_opt_dormentry_";

    String KEY_REDIS_DORMSHOP_DELIVERY_     = "print_dormshop_delivery_";
    String KEY_REDIS_DORMSHOPPRICE_PREFIX   = "print_dorm_shop_price_"; //缓存店铺价格前缀
    // Signin Path
    String PATH_ACCOUNT_CENTER_SIGNIN       = "/signin";
    String PATH_ACCOUNT_CENTER_SIGNIN_CHECK = PATH_ACCOUNT_CENTER_SIGNIN + "/check";
    String PATH_ACCOUNT_CENTER_SIGNOUT      = "/signout";

    //已废弃,改用服务层提供常量
    //by wxp(0 未付款，1 未打印，2 已打印，3 已完成，4 待退款， 5 已退款, 6 已取消)
    byte ORDER_STATUS_0 = 0;
    byte ORDER_STATUS_1 = 1;
    byte ORDER_STATUS_2 = 2;
    byte ORDER_STATUS_3 = 3;
    byte ORDER_STATUS_4 = 4;
    byte ORDER_STATUS_5 = 5;
    byte ORDER_STATUS_6 = 6;

    //配送方式
    byte DELIVERY_TYPE_DORMER   = 1;  //店长配送
    byte DELIVERY_TYPE_YOURSELF = 2;  //上门自取


    byte DELIVERY_STATUS_0 = 0; //送到寝室
    byte DELIVERY_STATUS_1 = 1; //送到楼下
    byte DELIVERY_STATUS_2 = 2; //只限自取

    byte DORMENTRY_STATUS_0 = 0; //未开通
    byte DORMENTRY_STATUS_1 = 1; //营业中
    byte DORMENTRY_STATUS_2 = 2; //可预定
    byte DORMENTRY_STATUS_3 = 3; //休息中

    byte SHOP_STATUS_0 = 0; //休息中
    byte SHOP_STATUS_1 = 1; //营业中
    byte SHOP_STATUS_2 = 2; //可预定

    byte SHOP_TYPE_PRINT = 2;

    int COUPON_EXPIRE_STATUS_0 = 0;  //未过期
    int COUPON_EXPIRE_STATUS_1 = 1;  //已过期

    int COUPON_ACTIVE_STATUS_0 = 0;  //未启用
    int COUPON_ACTIVE_STATUS_1 = 1;  //已启用

    //支付
    byte ORDER_PAY_ALIPAY       = 1;  //支付宝
    byte ORDER_PAY_WXPAY        = 2;  //微信h5支付
    byte ORDER_PAY_SPEND        = 3;  //59花
    byte ORDER_PAY_WXPAY_APP    = 6;  //微信支付app
    byte ORDER_PAY_WXPAY_CREDIT = 8;  //信用支付

    //广告 0不开启  1开启免费打印
    byte AD_STATUS_CLOSE = 0; //关闭
    byte AD_STATUS_OPEN  = 1; //开启
    byte AD_TYPE_FOOTER  = 1; //页脚广告
    byte AD_TYPE_FIRST   = 2; //首页广告

    //店铺状态
    byte DORMSHOP_STATUS_REST    = 0;  //休息中,关店
    byte DORMSHOP_STATUS_WORKING = 1;  //营业中,开店
    byte DORMSHOP_STATUS_AUTO    = 2;  //自动,可预定
    byte DORMSHOP_STATUS_STOP    = 9;  //停业

    //退款流水表订单类型
    byte ORDERPAY_RECORD_ORDER_TYPE     = 4;  //打印店
    //退款流水表订单记录状态
    byte ORDERPAY_RECORD_ORDER_STATUS_0 = 0;  //未处理
    byte ORDERPAY_RECORD_ORDER_STATUS_1 = 1;  //已处理

    //文档处理状态
    byte PDF_OPERATE_ING    = 0;  //文档处理中
    byte PDF_OPERATE_FINISH = 1;  //文档处理完成

    //是否订单的清单页面
    byte ORDER_FIRST_LIST_0 = 0;  //不是清单页面
    byte ORDER_FIRST_LIST_1 = 1;  //清单页面

    //空白页检测
    byte CHECK_STATUS_0 = 0;  //未检测
    byte CHECK_STATUS_1 = 1;  //检测通过
    byte CHECK_STATUS_2 = 2;  //检测失败
    
    String DORMSHOP_INI_URI="/common/dormShop";//拦截初始化配置URI
    String MESSAGE_TIME_INI="0";
    String MESSAGE_URI="/user/sendSms";	//待拦截URI-短信
    long MESSAGE_TIME_GAP=5000;//请求访问间隔
    long MESSAGE_OFFSET=24*60*60*1000;//radis过期时间
    String MESSAGE_DOMAIN="http://print.59store.com/";//refer初步验证
    String PDF_UPLOAD_URI="/order/sign";//待拦截URI-PDF上传
    //gala
    int GALA_VALIDATE_EXPIRE=1;
    int GALA_VALIDATE_ACTIVE=2;
    int GALA_VALIDATE_NUMBERS=3;
    String GALA_KEY_USER_PREFIX="print_gala_user_";
    String GALA_KEY_DORM_PREFIX="print_gala_dorm_";
    String GALA_KEY_ORDER_PREFIX="print_gala_order_";
    Integer GALA_AMOUNTS=1;
    //wechat
    String WECHAT_OPENID_PREFIX="print_";
    String WECHAT_OPENID_UID_PREFIX="print_uid_";
    String WECHAT_UID_OPENID_PREFIX="print_openid_";
    String WECHAT_TOKEN_PREFIX="print_wechat_token_a";
    String WECHAT_TOKEN_LOCK_PREFIX="print_wechat_token_lock";
    String[] CANCEL_REASON={"打印机开小差了，店长在努力维修ing","店长被童鞋们的打印热情包围了，纸/墨水已用完，补货中","任性的店长开启了一场说走就走的旅行模式，暂时不接单啦","店长在跟一大波作业做斗争，暂时不能接单了","其他"};
    
    String SITE_ID_NAME_PREFIX="wechat_site_id_";
    
    String KEY_REDIS_SHOP_APP                      = "print_shop_app_";            //缓存店铺前缀
    String PRINT_DORM_SITE_NAME="print_address_site_name_";
    String PATH_GENERATE_QRCODE	= "https://gfd.kuaixiumf.com/api/fn_entry";
}
