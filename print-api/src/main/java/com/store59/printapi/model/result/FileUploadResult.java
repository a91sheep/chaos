package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
public class FileUploadResult {
    private String page;
    private String pdf_path;
    private String md;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }
}
