/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.print.common.data;

/**
 * print常量值定义
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 16/1/22
 * @since 1.0
 */
public class PrintConst {

    /**
     * printOrder 常量定义
     **/
    //订单状态
    public final static byte ORDER_STATUS_INIT      = 0;        //新订单，未付款(文档处理中)
    public final static byte ORDER_STATUS_CONFIRMED = 1;        //文档转换完成
    public final static byte ORDER_STATUS_SENDING   = 2;        //配送中
    public final static byte ORDER_STATUS_FINISHED  = 4;        //订单最终完成
    public final static byte ORDER_STATUS_CANCELED  = 5;        //订单取消
    public final static byte ORDER_STATUS_PRINTED   = 6;        //已打印

    //订单来源
    public final static byte ORDER_SOURCE_PC      = 0;        //订单来源PC
    public final static byte ORDER_SOURCE_WEIXIN  = 1;        //订单来源手机网页(微信)
    public final static byte ORDER_SOURCE_SHOP    = 2;        //订单来源shop端
    public final static byte ORDER_SOURCE_IOS     = 3;        //订单来源IOS
    public final static byte ORDER_SOURCE_ANDROID = 4;        //订单来源ANDROID

    //配送方式
    public final static byte ORDER_DELIVERY_SEND = 1;         //店长配送
    public final static byte ORDER_DELIVERY_TAKE = 2;         //上门自取

    //支付方式
    public final static byte ORDER_PAY_ZFB        = 1;         //支付宝
    public final static byte ORDER_PAY_WEIXIN     = 6;         //微信支付
    public final static byte ORDER_PAY_CREDITCARD = 8;         //信用钱包
    /** printOrder 常量定义**/

    /**
     * printOrderDetail 常量定义
     **/
    //打印方式
    public final static byte PRINT_TYPE_BW_SINGLE    = 1;       //黑白单面
    public final static byte PRINT_TYPE_BW_DOUBLE    = 2;       //黑白双面
    public final static byte PRINT_TYPE_COLOR_SINGLE = 3;       //彩打单面
    public final static byte PRINT_TYPE_COLOR_DOUBLE = 4;       //彩打双面
    public final static byte PRINT_TYPE_PHOTO        = 5;                //照片打印

    //缩印方式
    public final static byte re_print_type_none     = 0;            //不缩印
    public final static byte re_print_type_two      = 1;            //二合一
    public final static byte re_print_type_four     = 2;            //四合一
    public final static byte re_print_type_six      = 3;            //六合一
    public final static byte re_print_type_nine     = 4;            //九合一
    public final static byte re_print_photo_default = 10;        //照片默认光面
    public final static byte re_print_photo_suede   = 11;            //照片绒面


    //pdf文档检测结果
    @Deprecated
    public final static byte pdf_check_status_none    = 0;      //未检测
    @Deprecated
    public final static byte pdf_check_status_success = 1;      //检测通过
    @Deprecated
    public final static byte pdf_check_status_fail    = 2;      //检测失败

    //检测状态
    public final static byte PDF_CHECK_STATUS_NO      = 0;      //未检测
    public final static byte PDF_CHECK_STATUS_SUCCESS = 1;      //检测通过
    public final static byte PDF_CHECK_STATUS_FAIL    = 2;      //检测未通过

    //是否返利
    public final static byte IS_PROFIT_NO  = 0;         //未返利
    public final static byte IS_PROFIT_YES = 1;         //已返利
    /**  printOrderDetail 常量定义 **/

    /**
     * 打印订单类型
     */
    public final static byte PRINT_ORDER_TYPE_PHOTO = 0;  //照片订单
    public final static byte PRINT_ORDER_TYPE_DOC   = 1;  //文档订单

}
