package com.store59.printapi.model.param.createOrder;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/15
 * @since 1.0
 */
public class CreateOrderDetailParam {
    private String pdf_path;
    private String pdf_md5;
    private Long pdf_size;
    private String doc_path;
    private String doc_md5;
    private String fileName;
    private Byte printType;
    private Byte reprintType;
    private Integer pageNum;
    private Integer num;

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getPdf_md5() {
        return pdf_md5;
    }

    public void setPdf_md5(String pdf_md5) {
        this.pdf_md5 = pdf_md5;
    }

    public Long getPdf_size() {
        return pdf_size;
    }

    public void setPdf_size(Long pdf_size) {
        this.pdf_size = pdf_size;
    }

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }

    public String getDoc_md5() {
        return doc_md5;
    }

    public void setDoc_md5(String doc_md5) {
        this.doc_md5 = doc_md5;
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
}
