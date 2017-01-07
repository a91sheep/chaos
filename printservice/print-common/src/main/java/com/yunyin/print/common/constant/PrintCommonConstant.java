package com.yunyin.print.common.constant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public interface PrintCommonConstant {
    //经营方式
    byte BUSINESS_TYPE_SELF = 1;     //自营
    byte BUSINESS_TYPE_JOIN = 2;     //加盟

    //机器状态
    byte MACHINE_STATUS_NORMAL   = 1;      //使用中（正常）
    byte MACHINE_STATUS_MAINTAIN = 2;    //维修中

    byte IS_ACTIVE_YES = 1;  //有效
    byte IS_ACTIVE_NO  = 0;  //无效

    //支付方式
    byte PAY_TYPE_ALIPAY = 1;   //支付宝
    byte PAY_TYPE_WEIXIN = 2;   //微信支付

    //文档是否处理完成
    byte DOC_DEAL_FINISHED = 1;    //文档处理完成
    byte DOC_DEALING       = 0;    //文档处理中

    //是否清单页
    byte IS_FIRST_NO  = 0;  //否
    byte IS_FIRST_YES = 1;  //是

    //文档类型
    byte DOC_TYPE_DOCUMENT = 1; //文档
    byte DOC_TYPE_PIC      = 2; //照片

    byte PRINT_TYPE_BLACK_WHITE = 1;  //黑白
    byte PRINT_TYPE_COLORFUL    = 2;  //彩色

    //缩印类型
    byte MINIMO_TYPE_NO   = 1;    //不缩印
    byte MINIMO_TYPE_TWO  = 2;    //二合一
    byte MINIMO_TYPE_FOUR = 3;    //四合一

    //纸张类型
    byte PAPER_TYPE_A4 = 1; //a4
    byte PAPER_TYPE_A6 = 1; //a6

    byte PRINT_SIDE_SINGLE = 1; //单面
    byte PRINT_SIDE_DOUBLE = 2; //双面

}
