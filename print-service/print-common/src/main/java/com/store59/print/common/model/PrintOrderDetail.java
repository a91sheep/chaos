/**
 *
 */
package com.store59.print.common.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月25日
 * @since 1.1
 */
@SuppressWarnings("serial")
public class PrintOrderDetail implements Serializable {
    private Long    orderDetailId; // 订单详情id
    private String  orderId;        // 订单号,19位Long类型id＋买家id后4位
    private Byte    status;        // 文档处理状态  0:文档处理中  1:文档处理完成
    private Byte    isFirst;       // 是否订单首页  0:否  1:是
    private String  sourceUrl;     // 原始文件的url
    private String  sourceMD5;     // 原始文件的md5
    private String  url;           // 文件url
    private String  pdfMD5;        // 转换后的文件的md5
    private Long    pdfSize;       // 转换后的文件大小
    private String  fileName;      // 文件名
    private Byte    printType;     // 打印形式,1:黑白单面 2:黑白双面 3:彩打单面 4:彩打双面 5:照片打印
    private Byte    reprintType;   // 缩印 （0 不缩印、1 二合一、2 四合一、3 六合一、4 九合一)
    private Integer pageNum;       // pdf页数
    private Integer num;           // 份数
    private Float   amount;        // 价格
    private Byte    checkStatus;   // 检查状态
    private String  checkMsg;
    private Long    checkTime;
    private Byte    isProfit;      // 是否返利

    //常量定义

    //检测状态
    public final static byte CHECK_STATUS_NO      = 0;      //未检测
    public final static byte CHECK_STATUS_SUCCESS = 1;      //检测通过
    public final static byte CHECK_STATUS_FAIL    = 2;      //检测未通过

    //是否返利
    public final static byte IS_PROFIT_NO  = 0;         //未返利
    public final static byte IS_PROFIT_YES = 1;         //已返利

    public String getCheckMsg() {
        return checkMsg;
    }

    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Byte isFirst) {
        this.isFirst = isFirst;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte getPrintType() {
        return printType;
    }

    public void setPrintType(Byte printType) {
        this.printType = printType;
    }

    public Byte getReprintType() {
        return reprintType;
    }

    public void setReprintType(Byte reprintType) {
        this.reprintType = reprintType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceMD5() {
        return sourceMD5;
    }

    public void setSourceMD5(String sourceMD5) {
        this.sourceMD5 = sourceMD5;
    }

    public String getPdfMD5() {
        return pdfMD5;
    }

    public void setPdfMD5(String pdfMD5) {
        this.pdfMD5 = pdfMD5;
    }

    public Long getPdfSize() {
        return pdfSize;
    }

    public void setPdfSize(Long pdfSize) {
        this.pdfSize = pdfSize;
    }

    public Byte getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Byte checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Byte getIsProfit() {
        return isProfit;
    }

    public void setIsProfit(Byte isProfit) {
        this.isProfit = isProfit;
    }
}
