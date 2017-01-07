package com.yunyin.print.common.model;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/12
 * @since 1.0
 */
public class PrintOrderDetail {
    private Long    id;            // 订单详情id
    private String  orderId;        // 订单号,19位Long类型id＋买家id后4位
    private Byte    printStatus;        // 文档处理状态  0:文档处理中  1:文档处理完成
    private Byte    isFirst;       // 是否订单首页  0:否  1:是
    private String  sourceUrl;     // 原始文件的url
    private String  sourceMD5;     // 原始文件的md5
    private String  pdfUrl;           // pdf url
    private String  pdfMD5;        // pdf的md5
    private String  fileName;      // 文件名
    private Byte    docType;       // 附件类型 1、文档 2、照片
    private Byte    printType;     // 打印类型  1、黑白 2、彩色
    private Byte    minimoType;   // 缩印类型  1、不缩印 2、二合一 3、四合一
    private Byte    paperType;     // 打印纸规格  1-A4 2-A6
    private Byte    printSide;    // 单双面  1-单面 2-双面
    private Integer pdfNum;       // pdf页数
    private Integer printNum;           // 份数
    private Float   amount;        // 价格

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Byte getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Byte printStatus) {
        this.printStatus = printStatus;
    }

    public Byte getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Byte isFirst) {
        this.isFirst = isFirst;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfMD5() {
        return pdfMD5;
    }

    public void setPdfMD5(String pdfMD5) {
        this.pdfMD5 = pdfMD5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte getDocType() {
        return docType;
    }

    public void setDocType(Byte docType) {
        this.docType = docType;
    }

    public Byte getPrintType() {
        return printType;
    }

    public void setPrintType(Byte printType) {
        this.printType = printType;
    }

    public Byte getMinimoType() {
        return minimoType;
    }

    public void setMinimoType(Byte minimoType) {
        this.minimoType = minimoType;
    }

    public Byte getPaperType() {
        return paperType;
    }

    public void setPaperType(Byte paperType) {
        this.paperType = paperType;
    }

    public Byte getPrintSide() {
        return printSide;
    }

    public void setPrintSide(Byte printSide) {
        this.printSide = printSide;
    }

    public Integer getPdfNum() {
        return pdfNum;
    }

    public void setPdfNum(Integer pdfNum) {
        this.pdfNum = pdfNum;
    }

    public Integer getPrintNum() {
        return printNum;
    }

    public void setPrintNum(Integer printNum) {
        this.printNum = printNum;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
