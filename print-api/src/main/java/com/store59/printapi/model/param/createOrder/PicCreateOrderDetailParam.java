package com.store59.printapi.model.param.createOrder;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月12日 下午2:10:10 
 * 类说明
 */
public class PicCreateOrderDetailParam {
    private String  pdf_path;
    private String  pdf_md5;
    private Long    pdf_size;
    private String  doc_path;
    private String  doc_md5;
    private String  file_name;
    private Byte    print_type;
    private Byte    reduced_type;  //缩印
    private Integer page;  //缩印前pdf页数
    private Integer print_page;  //缩印后pdf页数
    private Integer quantity;  //打印份数
    private Double  price;  //单份总价格
    private Double  origin_price;  //单份原价,默认等于price
    private Double  amount;  //文档金额=price * quantity
    private String  specifications;  //打印规格描述，页数指单份的页数
    
    public Byte getReduced_type() {
		return reduced_type;
	}

	public void setReduced_type(Byte reduced_type) {
		this.reduced_type = reduced_type;
	}

	public Integer getPrint_page() {
        return print_page;
    }

    public void setPrint_page(Integer print_page) {
        this.print_page = print_page;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(Double origin_price) {
        this.origin_price = origin_price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
