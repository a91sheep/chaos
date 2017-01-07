package com.store59.printapi.model.result.app;

import java.util.List;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月24日 上午10:00:16 
 * 类说明
 */
public class ShopSetPic {
    private List<ShopPrint> print_types;//照片样式
    private List<ShopPrice> size_types;//照片尺寸
	public List<ShopPrint> getPrint_types() {
		return print_types;
	}
	public void setPrint_types(List<ShopPrint> print_types) {
		this.print_types = print_types;
	}
	public List<ShopPrice> getSize_types() {
		return size_types;
	}
	public void setSize_types(List<ShopPrice> size_types) {
		this.size_types = size_types;
	}

    
}
