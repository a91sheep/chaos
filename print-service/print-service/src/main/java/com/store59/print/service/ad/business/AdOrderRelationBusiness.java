package com.store59.print.service.ad.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.store59.print.common.filter.AdOrderRelationFilter;
import com.store59.print.common.filter.LineCharFilter;
import com.store59.print.common.model.ad.AdOrder;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.model.ad.AdStatistics;
import com.store59.print.common.model.ad.LineChar;
import com.store59.print.common.model.ad.OrderStatistics;
import com.store59.print.dao.ad.AdOrderDao;
import com.store59.print.dao.ad.AdOrderRelationDao;

/**
 * Created on 2016-08-17.
 */
@Service
public class AdOrderRelationBusiness {
	@Autowired
	private AdOrderRelationDao adOrderRelationDao;
	@Autowired
	private AdOrderDao adOrderDao;

	public AdOrderRelation findAdOrderRelation(Long id) {
		return adOrderRelationDao.findAdOrderRelation(id);
	}

	public List<AdOrderRelation> findAdOrderRelationList(AdOrderRelationFilter filter) {
		return adOrderRelationDao.findAdOrderRelationList(filter);
	}

	public AdOrderRelation addAdOrderRelation(AdOrderRelation record) {
		return adOrderRelationDao.addAdOrderRelation(record);
	}

	public Boolean updateAdOrderRelation(AdOrderRelation record) {
		return adOrderRelationDao.updateAdOrderRelation(record);
	}

	public Boolean deleteAdOrderRelation(Long id) {
		return adOrderRelationDao.deleteAdOrderRelation(id);
	}

	public AdStatistics statistics(String orderId, Long uid) {
		AdStatistics adsta = new AdStatistics();
		OrderStatistics home = new OrderStatistics();
		OrderStatistics footer = new OrderStatistics();
		AdOrder adOrder = adOrderDao.findAdOrder(orderId, uid);
		Boolean homeTmp = false, footerTmp = false;
		if (adOrder.getHomeAdNum() > 0) {
			homeTmp = true;
		}
		if (adOrder.getFooterAdNum() > 0) {
			footerTmp = true;
		}
		if (homeTmp) {
			Integer hometimes = adOrderRelationDao.count(orderId, uid, 1);
			home.setTimes(hometimes);
			home.setPeople(adOrderRelationDao.countByPerson(orderId, uid, 1));
			home.setLeftpages((adOrder.getHomeAdNum() - hometimes) < 0 ? 0 : (adOrder.getHomeAdNum() - hometimes));
			home.setAdTotalNum(adOrder.getHomeAdNum());
			LineCharFilter lineFilter = new LineCharFilter();
			lineFilter.setOrderId(orderId);
			lineFilter.setType(1);
			lineFilter.setUid(uid);
			List<LineChar> lineChart = adOrderRelationDao.lineChar(lineFilter);
			home.setLinechart(lineChart);
			adsta.setHome(home);

		}
		if (footerTmp) {
			Integer footertimes = adOrderRelationDao.count(orderId, uid, 2);
			footer.setTimes(footertimes);
			footer.setPeople(adOrderRelationDao.countByPerson(orderId, uid, 2));
			footer.setLeftpages(
					(adOrder.getFooterAdNum() - footertimes) < 0 ? 0 : (adOrder.getFooterAdNum() - footertimes));
			footer.setAdTotalNum(adOrder.getFooterAdNum());
			LineCharFilter lineFilter = new LineCharFilter();
			lineFilter.setOrderId(orderId);
			lineFilter.setType(2);
			lineFilter.setUid(uid);
			List<LineChar> lineChart = adOrderRelationDao.lineChar(lineFilter);
			home.setLinechart(lineChart);
			adsta.setFooter(footer);
		}
		return adsta;
	}

	public List<LineChar> lineChar(LineCharFilter filter) {
		List<LineChar> lineChart = adOrderRelationDao.lineChar(filter);
		return lineChart;
	}

	public Integer addAdOrderRelationList(List<AdOrderRelation> list) {
		Integer result = 0;
		for (AdOrderRelation record : list) {
			adOrderRelationDao.addAdOrderRelation(record);
			result++;
		}
		return result;
	}

	public Integer getTotalAmount(AdOrderRelationFilter filter) {
		return adOrderRelationDao.getTotalAmount(filter);
	}

	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	public void weight() {
		adOrderRelationDao.weight();
	}
}
