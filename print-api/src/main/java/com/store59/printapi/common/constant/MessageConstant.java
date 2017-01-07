package com.store59.printapi.common.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
// 参数校验的信息
public interface MessageConstant {

    String LOGIN_CALLBACK_PARAMS_BLANK = "请求参数smartTicket不能为空";
    String LOGIN_SIGNIN_INVALID        = "账号中心signin检查未通过";
    String USER_NORECORD               = "用户不存在";
    String WECHAT_HAS_BINDED		   = "用户已经绑定过";

    String PHONE_NUM_NOT_NULL          = "手机号不能为空";
    String SMS_CODE_NOT_NULL           = "手机验证码不能为空";

    String Dormentry_Id                = "楼栋Id不能为空";
    String Shop_Id                     = "店铺id不能为空";
    String City_Id                     = "城市id不能为空";
    String Zone_Id                     = "区id不能为空";
    String Site_Id                     = "学校id不能为空";
    String Province_Id                 = "省id不能为空";
    //订单表
    String ORDER_DELIVERYTYPE_PARAM_BLANK = "订单配送方式不能为空";
    String ORDER_PHONE_PARAM_BLANK = "用户手机号码不能为空";
    String ORDER_NAME_PARAM_BLANK = "用户姓名不能为空";
    String ORDER_ADDRESS_PARAM_BLANK = "用户地址不能为空";
    String ORDER_PAYTYPE_PARAM_BLANK = "支付方式不能为空";
    String ORDER_DORMID_PARAM_BLANK = "楼号不能为空";
    String ORDER_SHOPID_PARAM_BLANK = "店铺id不能为空";
    String ORDER_DETAILS_PARAM_BLANK = "订单列表不能为空";
    String ORDER_TYPE_PARAM_BLANK	="订单类型不能为空";

    String ORDER_ID_PARAM_BLANK = "订单号不能为空";

    //优惠券
    String IS_COUPON_ALL = "是否获取用户全部优惠券标志不能为空!";
    String COUPON_TYPE_BLANK="用户优惠券类型不能为空";

    //app
    String APP_TOKEN_NOT_BLANK = "token不能为空!";
    //pay
    String PAY_WXPAY_OPENID_BLANK="微信openid不能为空";
}
