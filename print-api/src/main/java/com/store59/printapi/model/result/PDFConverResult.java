package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/2/25
 * @since 1.0
 */
public class PDFConverResult {
    private String  pdf_path;
    private String  pdf_md5;
    private Integer pdf_number;

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

    public Integer getPdf_number() {
        return pdf_number;
    }

    public void setPdf_number(Integer pdf_number) {
        this.pdf_number = pdf_number;
    }
}
