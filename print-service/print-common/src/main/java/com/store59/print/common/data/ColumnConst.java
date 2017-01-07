package com.store59.print.common.data;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/5
 * @since 1.0
 */
public class ColumnConst {
    /**
     * 打印订单表扩展字段
     */
    public final static String DELIVERY_TYPE      = "delivery_type";    //配送方式
    public final static String AD_PAGE_NUM        = "ad_page_num";      //福利纸使用张数
    public final static String AD_UNIT_PRICE      = "ad_unit_price";    //福利纸结算单张价格
    public final static String DOC_AMOUNT         = "doc_amount";       //文档价格= 文档计算金额 + 福利纸优惠金额
    public final static String DORM_ID            = "dorm_id";
    public final static String AUTO_CONFIRM_HOURS = "auto_confirm_hours";   //自动确认收货时间

    /**
     * 打印订单详情表扩展字段
     */
    public final static String STATUS       = "status";         //文档处理状态(0:处理中  1:处理完成)
    public final static String IS_FIRST     = "is_first";       //是否清单广告页(0:否  1:是)
    public final static String SOURCE_URL   = "source_url";     //原始文档url
    public final static String SOURCE_MD5   = "source_md5";     //原始文档md5
    public final static String URL          = "url";            //转换后pdf文档url
    public final static String PDF_MD5      = "pdf_md5";        //转换后pdf文档md5
    public final static String PDF_SIZE     = "pdf_size";       //转换后pdf文档的大小
    public final static String PRINT_TYPE   = "print_type";     //打印方式(1:黑白单面  2:黑白双面  3:彩色单面  4:彩色双面)
    public final static String REPRINT_TYPE = "reprint_type";   //缩印样式(0:不缩印  1:二合一  2:四合一  3:六合一  4:九合一  10:照片光面  11:照片绒面)
    public final static String PAGE_NUM     = "page_num";       //pdf页数
    public final static String CHECK_STATUS = "check_status";   //pdf文档检测结果(0:未检测  1:检测通过  2:检测失败)
    public final static String CHECK_MSG    = "check_msg";      //pdf文档检测描述
    public final static String CHECK_TIME   = "check_time";     //pdf文档检测时间
    public final static String IS_PROFIT    = "is_profit";      //是否返利(0:未返利  1:已返利)

    /**
     * 阿里云domain
     */
    public final static String OSS_DOMAIN_NAME = "http://print-identification.oss-cn-hangzhou.aliyuncs.com";
}
