package com.store59.printapi.service.createOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.base.common.api.SiteApi;
import com.store59.base.common.model.Site;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.filter.AdOrderFilter;
import com.store59.print.common.model.ad.AdFreeOrder;
import com.store59.print.common.model.ad.AdOrder;
import com.store59.print.common.model.ad.AdOrderRelation;
import com.store59.print.common.remoting.AdOrderService;
import com.store59.printapi.common.constant.AdConstant;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.ad.AdFreeParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderParam;
import com.store59.printapi.model.param.createOrder.CreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.CreateOrderDetailsList;
import com.store59.printapi.model.param.createOrder.CreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.ad.AdFooterMsg;
import com.store59.printapi.model.result.ad.AdFreeInfo;
import com.store59.printapi.model.result.order.AdFreeResult;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/4/20
 * @since 1.0
 */
@Service
public class AdService {
	@Value("${store59.footer.num.limit}")
	private int homeFreeNum;
	@Autowired
	AdOrderService adService;
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;
	@Autowired
	SiteApi siteApi;
	@Value("${store59.footer.num.ad1}")
	private String footerUrlAd1;
	@Value("${store59.footer.num.ad2}")
	private String footerUrlAd2;
	@Value("${store59.footer.num.ad3}")
	private String footerUrlAd3;
	@Value("${store59.footer.num.ad4}")
	private String footerUrlAd4;
	@Value("${store59.footer.num.ad5}")
	private String footerUrlAd5;
//	private String interlsplit = "===qwe123===";

	public Datagram<?> getAdFreeAmount(AdFreeParam param,long uid) {
		CreateOrderDetailsList list = new CreateOrderDetailsList();
		try {
			list = JsonUtil.getObjectFromJson(param.getDetails(), CreateOrderDetailsList.class);
		} catch (Exception e) {
			System.out.println("*******Details:" + param.getDetails());
			throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
		}

		int bw_page_num = 0;
		int free_page_num = 0;
		// step1 是否有黑白打印的纸张
		boolean hasBlackWhite = false;
		for (CreateOrderDetailParam createOrderDetailParam : list.getDetails()) {
			if (PrintConst.PRINT_TYPE_BW_SINGLE == createOrderDetailParam.getPrintType().byteValue()
					|| PrintConst.PRINT_TYPE_BW_DOUBLE == createOrderDetailParam.getPrintType().byteValue()) {
				hasBlackWhite = true;

				Integer pdf_pageNum = createOrderDetailParam.getPageNum(); // pdf页数
				Double final_pageNum = 0.00;
				// 是否缩印
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_two) { // 二合一
					final_pageNum = Math.ceil(pdf_pageNum / 2.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_four) { // 四合一
					final_pageNum = Math.ceil(pdf_pageNum / 4.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_six) { // 六合一
					final_pageNum = Math.ceil(pdf_pageNum / 6.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_nine) { // 九合一
					final_pageNum = Math.ceil(pdf_pageNum / 9.0);
				}
				// 考虑单双页
				if (createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
						|| createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
					if (createOrderDetailParam.getReprintType().byteValue() == PrintConst.re_print_type_none) {
						final_pageNum = Math.ceil(pdf_pageNum / 2.0);
					} else {
						final_pageNum = Math.ceil(final_pageNum / 2.0);
					}
				}
				// 黑白纸张等于单份文档的张数
				if (final_pageNum.doubleValue() == 0) {
					bw_page_num += pdf_pageNum;
				} else {
					bw_page_num += final_pageNum.intValue();
				}
			}
		}
		if (!hasBlackWhite) {
			throw new BaseException(CommonConstant.STATUS_FREE_PRINT_UNABLE,
					CommonConstant.MSG_STATUS_FREE_PRINT_UNABLE);
		}

		// step2 是否有广告商
		// AdTypeFilter adTypeFilter = new AdTypeFilter();
		// List<Integer> siteIdList = new ArrayList<>();
		// siteIdList.add(param.getSiteId());
		// adTypeFilter.setSiteList(siteIdList);
		// adTypeFilter.setOffset(0);
		// adTypeFilter.setLimit(homeFreeNum);
		// Result<FirstAdDetail> adRet =
		// adApi.getFirstAdDetailByFilter(adTypeFilter);
		// if (adRet.getStatus() != 0 || adRet.getData() == null) {
		// throw new
		// BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
		// CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
		// }
		// List<Ad> footerAdList = adRet.getData().getFooterList();
		// Ad firstAd = adRet.getData().getFirsetAd();

		// step2 new
		AdOrderFilter filter = new AdOrderFilter();
		filter.setSiteId(param.getSiteId());
		filter.setOffset(0);
		filter.setLimit(bw_page_num);
		filter.setUid(uid);
		Result<AdFreeOrder> adRet = adService.findFreeOrder(filter);
		List<AdOrder> footerAdList = adRet.getData().getFooterOrder();
		AdOrder firstAd = adRet.getData().getHomeOrder();
		// new over

		boolean hasAds = false;
		if ((firstAd != null && firstAd.getId() != null) || footerAdList.size() > 0) {
			hasAds = true;
		}
		if (!hasAds) {
			// 没有广告量了
			throw new BaseException(CommonConstant.STATUS_FREE_PRINT_UNABLE,
					CommonConstant.MSG_STATUS_FREE_PRINT_UNABLE);
		}
		// step3 是否有页脚广告
		int footerAdSize = footerAdList.size();// 页脚广告数量
		// step4 是否有首页广告,无:打印59首页广告
		if (firstAd != null && firstAd.getId() != null) {// 有首页广告
			if (bw_page_num <= homeFreeNum)
				free_page_num = Math.min(homeFreeNum, bw_page_num);
			if (bw_page_num > homeFreeNum) {
				if (footerAdSize <= homeFreeNum) {
					free_page_num = homeFreeNum;
				} else {
					free_page_num = Math.min(footerAdSize, bw_page_num);
				}
			}
		} else {// 没有首页广告
			free_page_num = Math.min(bw_page_num, footerAdSize);
		}
		AdFreeInfo adFreeInfo = new AdFreeInfo();
		adFreeInfo.setFreePageNum(free_page_num);

		return DatagramHelper.getDatagramWithSuccess(adFreeInfo);
	}

	public AdFreeResult calFreePages(CreateOrderParam param, Integer siteId, Map<String, Object> paramMap, Long cUid) {
		adService.updateFinished();
		CreateOrderDetailsList list = new CreateOrderDetailsList();
		try {
			list = JsonUtil.getObjectFromJson(param.getDetails(), CreateOrderDetailsList.class);
		} catch (Exception e) {
			System.out.println("*******Details:" + param.getDetails());
			throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
		}
		AdFreeResult adResult = new AdFreeResult();

		int bw_page_num = 0;
		int free_page_num = 0;
		List<AdOrderRelation> order = new ArrayList<AdOrderRelation>();
		/**
		 * 广告打印优先使用黑白单面,由于现在没有双面,后期开放双面,需要对免费价格重新计算!!!!!
		 */
		// 如果选择免费打印-广告
		paramMap.put("footerAd1", footerUrlAd1); // 默认传递59的页脚广告
		paramMap.put("footerAd2", footerUrlAd2);
		paramMap.put("footerAd3", footerUrlAd3);
		paramMap.put("footerAd4", footerUrlAd4);
		paramMap.put("footerAd5", footerUrlAd5);
		// 下单第一步先判断是否有免费打印资格
		// step1 是否有黑白打印的纸张
		boolean hasBlackWhite = false;
		for (CreateOrderDetailParam createOrderDetailParam : list.getDetails()) {
			if (PrintConst.PRINT_TYPE_BW_SINGLE == createOrderDetailParam.getPrintType().byteValue()
					|| PrintConst.PRINT_TYPE_BW_DOUBLE == createOrderDetailParam.getPrintType().byteValue()) {
				hasBlackWhite = true;

				Integer pdf_pageNum = createOrderDetailParam.getPageNum(); // pdf页数
				Double final_pageNum = 0.00;
				// 是否缩印
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_two) { // 二合一
					final_pageNum = Math.ceil(pdf_pageNum / 2.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_four) { // 四合一
					final_pageNum = Math.ceil(pdf_pageNum / 4.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_six) { // 六合一
					final_pageNum = Math.ceil(pdf_pageNum / 6.0);
				}
				if (createOrderDetailParam.getReprintType() == PrintConst.re_print_type_nine) { // 九合一
					final_pageNum = Math.ceil(pdf_pageNum / 9.0);
				}
				// 考虑单双页
				if (createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
						|| createOrderDetailParam.getPrintType().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
					if (createOrderDetailParam.getReprintType().byteValue() == PrintConst.re_print_type_none) {
						final_pageNum = Math.ceil(pdf_pageNum / 2.0);
					} else {
						final_pageNum = Math.ceil(final_pageNum / 2.0);
					}
				}
				// 黑白纸张等于单份文档的张数
				if (final_pageNum.doubleValue() == 0) {
					bw_page_num += pdf_pageNum;
				} else {
					bw_page_num += final_pageNum.intValue();
				}
			}
		}
		if (!hasBlackWhite) {
			adResult.setFreePages(0);
			return adResult;
		}

		// step2 是否有广告商

		AdOrderFilter filter = new AdOrderFilter();
		filter.setSiteId(siteId);
		filter.setOffset(0);
		filter.setLimit(bw_page_num);
		filter.setUid(cUid);
		Result<AdFreeOrder> adRet = adService.updateInventory(filter);
		if (adRet.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取广告商信息出错" + JsonUtil.getJsonFromObject(adRet));
		}
		List<AdOrder> footerAdList = adRet.getData().getFooterOrder();
		AdOrder firstAd = adRet.getData().getHomeOrder();

		boolean hasAds = false;
		if ((firstAd != null && firstAd.getId() != null) || footerAdList.size() > 0) {
			hasAds = true;
		}
		if (!hasAds) {
			// 没有广告量了
			throw new BaseException(CommonConstant.STATUS_FREE_PRINT_UNABLE,
					CommonConstant.MSG_STATUS_FREE_PRINT_UNABLE);
		}
		// step3 是否有页脚广告
		int footerAdSize = footerAdList.size();// 页脚广告数量
		// step4 是否有首页广告,无:打印59首页广告
		boolean hasFirst = true;
		if (firstAd != null && firstAd.getId() != null) {// 有首页广告
			if (bw_page_num <= homeFreeNum)
				free_page_num = Math.min(homeFreeNum, bw_page_num);
			if (bw_page_num > homeFreeNum) {
				if (footerAdSize <= homeFreeNum) {
					free_page_num = homeFreeNum;
				} else {
					free_page_num = Math.min(footerAdSize, bw_page_num);
				}
			}
		} else {// 没有首页广告
			hasFirst = false;
			free_page_num = Math.min(bw_page_num, footerAdSize);
		}
		paramMap.put("freePageNum", free_page_num);
		// 更新实体的逻辑
		// 1:如果有首页广告,第一个首页广告Ad减1
		// 2:free_page_num与footerAdList.size()比较,使用了几个页脚,就更新前几个的页脚广告实体的投放量
		// List<Ad> updateAdList = new ArrayList<>();
		String siteName = valueOpsCache.get(CommonConstant.SITE_ID_NAME_PREFIX + siteId);
		if (com.store59.printapi.common.utils.StringUtil.isBlank(siteName)) {
			Result<Site> siteInfo = siteApi.getSite(siteId);
			if (siteInfo.getStatus() != 0) {
				throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
						"查询学校信息失败：" + JsonUtil.getJsonFromObject(siteInfo) + "params：" + (siteId));
			}
			siteName = siteInfo.getData() == null ? null : siteInfo.getData().getSiteName();
		}
		if (hasFirst) {
			AdOrderRelation adrla = new AdOrderRelation();
			adrla.setPageNum(1);
			adrla.setcUid(cUid);
			adrla.setSiteId(Long.valueOf(siteId));
			adrla.setType(1);
			adrla.setUid(firstAd.getUid());
			adrla.setSiteName(siteName);
			adrla.setOrderId(firstAd.getOrderId());
			order.add(adrla);
			paramMap.put("firstPageUrl", firstAd.getHomePageImage());
		}
		int footUsedNum = Math.min(free_page_num, footerAdSize);
		int j = 0;
		for (int i = 0; i < footUsedNum; i++) {
			AdOrderRelation adrla = new AdOrderRelation();
			adrla.setPageNum(1);
			adrla.setcUid(cUid);
			adrla.setSiteId(Long.valueOf(siteId));
			adrla.setType(2);
			adrla.setUid(footerAdList.get(i).getUid());
			adrla.setSiteName(siteName);
			adrla.setOrderId(footerAdList.get(i).getOrderId());
			order.add(adrla);
			j = i + 1;
			if(footerAdList.get(i).getEditPdf()!=null&&!footerAdList.get(i).getEditPdf().equals(""))
				paramMap.put("footerAd" + j,
						footerAdList.get(i).getEditPdf() );
		}
		// 遍历
		List<AdFooterMsg> adFooterList = new ArrayList<>();
		for (int i = 1; i <= (j < 5 ? 5 : j); i++) {
			String ad = paramMap.get("footerAd" + i) == null ? null : paramMap.get("footerAd" + i).toString();
			if (ad != null) {
//				String[] info = ad.split(interlsplit);
				AdFooterMsg msg = new AdFooterMsg();
//				if (info.length <= 1) {
//					msg.setSlogan(ad);
//				} else {
					msg.setImg(ad);
//					msg.setSlogan(info[1]);
//				}
				adFooterList.add(msg);
			}
		}
		paramMap.put("adFooterList", JsonUtil.getJsonFromObject(adFooterList));
		// 处理页脚广告结束
		adResult.setList(order);
		adResult.setFreePages(free_page_num);
		return adResult;
	}
	/**
	 * 计算app免费打印广告数据
	 * @param param
	 * @param siteId
	 * @param paramMap
	 * @param cUid
	 * @param type 1购物车不减库存，2下单减库存
	 * @return
	 */
	public AdFreeResult calFreePagesApp(AppCreateOrderParam param, Integer siteId, Map<String, Object> paramMap,
			Long cUid,int type) {
		adService.updateFinished();
		List<AppCreateOrderDetailParam> list = JsonUtil.getObjectFromJson(param.getItems(),
				new TypeReference<List<AppCreateOrderDetailParam>>() {
				});
		AdFreeResult adResult = new AdFreeResult();

		int bw_page_num = 0;
		int free_page_num = 0;
		List<AdOrderRelation> order = new ArrayList<AdOrderRelation>();
		/**
		 * 广告打印优先使用黑白单面,由于现在没有双面,后期开放双面,需要对免费价格重新计算!!!!!
		 */
		// 如果选择免费打印-广告
		paramMap.put("footerAd1", footerUrlAd1); // 默认传递59的页脚广告
		paramMap.put("footerAd2", footerUrlAd2);
		paramMap.put("footerAd3", footerUrlAd3);
		paramMap.put("footerAd4", footerUrlAd4);
		paramMap.put("footerAd5", footerUrlAd5);
		// 下单第一步先判断是否有免费打印资格
		// step1 是否有黑白打印的纸张
		boolean hasBlackWhite = false;
		for (AppCreateOrderDetailParam createOrderDetailParam : list) {
			if (PrintConst.PRINT_TYPE_BW_SINGLE == createOrderDetailParam.getPrint_type().byteValue()
					|| PrintConst.PRINT_TYPE_BW_DOUBLE == createOrderDetailParam.getPrint_type().byteValue()) {
				hasBlackWhite = true;

				Integer pdf_pageNum = createOrderDetailParam.getPage(); // pdf页数
				Double final_pageNum = 0.00;
				// 是否缩印
				if (createOrderDetailParam.getReduced_type() == PrintConst.re_print_type_two) { // 二合一
					final_pageNum = Math.ceil(pdf_pageNum / 2.0);
				}
				if (createOrderDetailParam.getReduced_type() == PrintConst.re_print_type_four) { // 四合一
					final_pageNum = Math.ceil(pdf_pageNum / 4.0);
				}
				if (createOrderDetailParam.getReduced_type() == PrintConst.re_print_type_six) { // 六合一
					final_pageNum = Math.ceil(pdf_pageNum / 6.0);
				}
				if (createOrderDetailParam.getReduced_type() == PrintConst.re_print_type_nine) { // 九合一
					final_pageNum = Math.ceil(pdf_pageNum / 9.0);
				}
				// 考虑单双页
				if (createOrderDetailParam.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
						|| createOrderDetailParam.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
					if (createOrderDetailParam.getReduced_type().byteValue() == PrintConst.re_print_type_none) {
						final_pageNum = Math.ceil(pdf_pageNum / 2.0);
					} else {
						final_pageNum = Math.ceil(final_pageNum / 2.0);
					}
				}
				// 黑白纸张等于单份文档的张数
				if (final_pageNum.doubleValue() == 0) {
					bw_page_num += pdf_pageNum;
				} else {
					bw_page_num += final_pageNum.intValue();
				}
			}
		}
		if (!hasBlackWhite) {
			adResult.setFreePages(0);
			return adResult;
		}

		// step2 是否有广告商

		AdOrderFilter filter = new AdOrderFilter();
		filter.setSiteId(siteId);
		filter.setOffset(0);
		filter.setUid(cUid);
		filter.setLimit(bw_page_num);
		Result<AdFreeOrder> adRet=null;
		if(type==1)
			adRet = adService.findFreeOrder(filter);
		if(type==2)
			adRet = adService.updateInventory(filter);
		if (adRet.getStatus() != 0) {
			throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
					"获取广告商信息出错" + JsonUtil.getJsonFromObject(adRet));
		}
		List<AdOrder> footerAdList = adRet.getData().getFooterOrder();
		AdOrder firstAd = adRet.getData().getHomeOrder();

		boolean hasAds = false;
		if ((firstAd != null && firstAd.getId() != null) || footerAdList.size() > 0) {
			hasAds = true;
		}
		if (!hasAds) {
			// 没有广告量了
			adResult.setFreePages(0);
			return adResult;
		}
		// step3 是否有页脚广告
		int footerAdSize = footerAdList.size();// 页脚广告数量
		// step4 是否有首页广告,无:打印59首页广告
		boolean hasFirst = true;
		if (firstAd != null && firstAd.getId() != null) {// 有首页广告
			if (bw_page_num <= homeFreeNum)
				free_page_num = Math.min(homeFreeNum, bw_page_num);
			if (bw_page_num > homeFreeNum) {
				if (footerAdSize <= homeFreeNum) {
					free_page_num = homeFreeNum;
				} else {
					free_page_num = Math.min(footerAdSize, bw_page_num);
				}
			}
		} else {// 没有首页广告
			hasFirst = false;
			free_page_num = Math.min(bw_page_num, footerAdSize);
		}
		paramMap.put("freePageNum", free_page_num);
		// 更新实体的逻辑
		// 1:如果有首页广告,第一个首页广告Ad减1
		// 2:free_page_num与footerAdList.size()比较,使用了几个页脚,就更新前几个的页脚广告实体的投放量
		// List<Ad> updateAdList = new ArrayList<>();
		String siteName = valueOpsCache.get(CommonConstant.SITE_ID_NAME_PREFIX + siteId);
		if (com.store59.printapi.common.utils.StringUtil.isBlank(siteName)) {
			Result<Site> siteInfo = siteApi.getSite(siteId);
			if (siteInfo.getStatus() != 0) {
				throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
						"查询学校信息失败：" + JsonUtil.getJsonFromObject(siteInfo) + "params：" + (siteId));
			}
			siteName = siteInfo.getData() == null ? null : siteInfo.getData().getSiteName();
		}
		if (hasFirst) {
			AdOrderRelation adrla = new AdOrderRelation();
			adrla.setPageNum(1);
			adrla.setcUid(cUid);
			adrla.setSiteId(Long.valueOf(siteId));
			adrla.setType(1);
			adrla.setUid(firstAd.getUid());
			adrla.setSiteName(siteName);
			adrla.setOrderId(firstAd.getOrderId());
			order.add(adrla);
			paramMap.put("firstPageUrl", firstAd.getHomePageImage());
		}
		int footUsedNum = Math.min(free_page_num, footerAdSize);
		int j = 0;
		for (int i = 0; i < footUsedNum; i++) {
			AdOrderRelation adrla = new AdOrderRelation();
			adrla.setPageNum(1);
			adrla.setcUid(cUid);
			adrla.setSiteId(Long.valueOf(siteId));
			adrla.setType(2);
			adrla.setUid(footerAdList.get(i).getUid());
			adrla.setSiteName(siteName);
			adrla.setOrderId(footerAdList.get(i).getOrderId());
			order.add(adrla);
			j = i + 1;
			if(footerAdList.get(i).getEditPdf()!=null&&!footerAdList.get(i).getEditPdf().equals(""))
			paramMap.put("footerAd" + j,
					footerAdList.get(i).getEditPdf() );
		}
		// 遍历
		List<AdFooterMsg> adFooterList = new ArrayList<>();
		for (int i = 1; i <= (j < 5 ? 5 : j); i++) {
			String ad = paramMap.get("footerAd" + i) == null ? null : paramMap.get("footerAd" + i).toString();
			if (ad != null) {
//				String[] info = ad.split(interlsplit);
				AdFooterMsg msg = new AdFooterMsg();
//				if (info.length <= 1) {
//					msg.setSlogan(ad);
//				} else {
					msg.setImg(ad);
//					msg.setSlogan(ad);
//				}
				adFooterList.add(msg);
			}
		}
		paramMap.put("adFooterList", JsonUtil.getJsonFromObject(adFooterList));
		//处理页脚广告结束
		adResult.setList(order);
		adResult.setFreePages(free_page_num);
		return adResult;
	}

}
