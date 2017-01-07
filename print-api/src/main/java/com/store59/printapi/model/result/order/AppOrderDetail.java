package com.store59.printapi.model.result.order;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/15
 * @since 1.0
 */
public class AppOrderDetail {
    private Integer order_detail_id; // 订单详情id
    private Long    order_id;       // 订单id
    private String  source_url;     // 原始文件的url
    private String  source_md5;     // 原始文件的md5
    private String  url;           // 文件url
    private String  pdf_md5;        // 转换后的文件的md5
    private Long    pdf_size;       // 转换后的文件大小
    private String  file_name;      // 文件名
    private Byte    print_type;     // 打印形式,1:黑白单面 2:黑白双面 3:彩打单面 4:彩打双面
    private Byte    reprint_type;   // 缩印 （0 不缩印、1 二合一、2 四合一、3 六合一、4 九合一)
    private Integer page_num;       // pdf页数
    private Integer num;           // 份数
    private Float   amount;        // 价格
    private Byte    check_status;   // 检查状态
    private Byte    is_profit;      // 是否返利

    public Integer getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(Integer order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getSource_md5() {
        return source_md5;
    }

    public void setSource_md5(String source_md5) {
        this.source_md5 = source_md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Byte getPrint_type() {
        return print_type;
    }

    public void setPrint_type(Byte print_type) {
        this.print_type = print_type;
    }

    public Byte getReprint_type() {
        return reprint_type;
    }

    public void setReprint_type(Byte reprint_type) {
        this.reprint_type = reprint_type;
    }

    public Integer getPage_num() {
        return page_num;
    }

    public void setPage_num(Integer page_num) {
        this.page_num = page_num;
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

    public Byte getCheck_status() {
        return check_status;
    }

    public void setCheck_status(Byte check_status) {
        this.check_status = check_status;
    }

    public Byte getIs_profit() {
        return is_profit;
    }

    public void setIs_profit(Byte is_profit) {
        this.is_profit = is_profit;
    }
}
