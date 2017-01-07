package com.store59.print.service.ad.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdOrder;
import com.store59.print.dao.ad.AdOrderDao;
import com.store59.print.tools.OrderIdTool;

/**
 * Created on 2016-08-17.
 */
@Service
public class AdOrderBusiness {
	@Autowired
	private AdOrderDao adOrderDao;

	public AdOrder findAdOrder(String id, Long uid) {
		return adOrderDao.findAdOrder(id, uid);
	}

	/**
	 * 查询订单列表
	 * 
	 * @param filter
	 * @return
	 */
	public List<AdOrder> findAdOrderList(AdOrderFilter filter) {
		return adOrderDao.findAdOrderList(filter);
	}

	/**
	 * 生成订单
	 * 
	 * @param record
	 * @return
	 */
	public AdOrder addAdOrder(AdOrder record) {
		record.setOrderId(String.valueOf(OrderIdTool.getPrintOderId()));
		return adOrderDao.addAdOrder(record);
	}

	public Boolean updateAdOrder(AdOrder record) {
		return adOrderDao.updateAdOrder(record);
	}

	public Boolean deleteAdOrder(Long id) {
		return adOrderDao.deleteAdOrder(id);
	}

	public List<AdOrder> freeAd(AdOrderFilter filter) {
		return adOrderDao.freeAd(filter);
	}

	public Integer getTotalAmount(AdOrderFilter filter) {
		return adOrderDao.getTotalAmount(filter);
	}

	public List<AdOrder> findOrderUsedList(AdOrderFilter filter) {
		return adOrderDao.findOrderUsedList(filter);
	}
}
