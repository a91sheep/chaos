package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/18
 * @since 1.0
 */
public class UrlResult {
    private String page;
    private String pdf_path;
    private String pdf_md5;
    private Long pdf_size;
    private String doc_path;
    private String doc_md5;
    private String doc_file_name;

    public String getDoc_file_name() {
        return doc_file_name;
    }

    public void setDoc_file_name(String doc_file_name) {
        this.doc_file_name = doc_file_name;
    }

    public Long getPdf_size() {
        return pdf_size;
    }

    public void setPdf_size(Long pdf_size) {
        this.pdf_size = pdf_size;
    }

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

    public String getPdf_md5() {
        return pdf_md5;
    }

    public void setPdf_md5(String pdf_md5) {
        this.pdf_md5 = pdf_md5;
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
}
