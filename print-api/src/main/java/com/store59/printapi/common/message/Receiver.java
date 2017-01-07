package com.store59.printapi.common.message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.OrderItem;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.remoting.OrderItemService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.model.result.ad.AdFooterMsg;
import com.store59.printapi.model.result.order.OrderCenterDTO;
import com.store59.printapi.service.OrderCenterService;
import com.store59.printapi.service.createOrder.PicCreateOrderService;
import com.store59.printapi.service.message.PdfService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/4/12
 * @since 1.0
 */
@Component
public class Receiver {
	// @Autowired
	// private PrintOrderService printOrderService;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private PicCreateOrderService orderService;
	@Autowired
	private OSSClient ossClient;
	@Value("${aliyun.oss.print.bucketName}")
	private String bucketName;
	private Logger logger = LoggerFactory.getLogger(Receiver.class);
	@Autowired
	private OrderCenterService orderCenterService;
	@Autowired
	private OrderItemService  itemService;
	public void receive(Object message) {
		logger.info("消息处理订单文档:" + message.toString());
		Map<String, Object> msg = JsonUtil.getObjectFromJson((String) message, Map.class);
		String orderId = (String) msg.get("orderId");
		try {
			String firstPageUrl = (String) msg.get("firstPageUrl");
			// String footerAd1 = (String) msg.get("footerAd1");
			// String footerAd2 = (String) msg.get("footerAd2");
			// String footerAd3 = (String) msg.get("footerAd3");
			// String footerAd4 = (String) msg.get("footerAd4");
			// String footerAd5 = (String) msg.get("footerAd5");
			// List<String> footerAdlist = new ArrayList<>();
			// footerAdlist.add(footerAd1);
			// footerAdlist.add(footerAd2);
			// footerAdlist.add(footerAd3);
			// footerAdlist.add(footerAd4);
			// footerAdlist.add(footerAd5);
			List<AdFooterMsg> footerAdlist = new ArrayList<>();
			try {
				if (msg.get("adFooterList") != null)
					footerAdlist = JsonUtil.getObjectFromJson(msg.get("adFooterList").toString(),
							new TypeReference<List<AdFooterMsg>>() {
							});
			} catch (Exception e) {
				logger.error(CommonConstant.MSG_CONVER_JSON + msg.get("adFooterList"));
			}
			int freePageNum = (int) msg.get("freePageNum");
			boolean hasFooterAd = freePageNum > 0 ? true : false;

			// Result<PrintOrder> result =
			// printOrderService.findByOrderId(orderId, true);
			// if (result == null || result.getStatus() != 0) {
			// logger.error("消息处理文档,获取订单信息失败!!!订单号:" + orderId);
			// throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
			// "消息处理文档,获取订单信息失败!!!订单号:" + orderId);
			//
			// }
			OrderCenterDTO orderCenterDTO = orderCenterService.getOrderByOrderId(orderId, true, true);
			PrintOrder printOrder = orderCenterDTO.getPrintOrder();
			// 需要生成清单广告页后,再处理详情文档,保证订单中包含广告页已经处理完成
			pdfService.createFirstSign(printOrder, firstPageUrl);

			// 订单状态为文档处理中
			List<PrintOrderDetail> list = printOrder.getDetails();
			// TODO 由于黑白打印包含单双面,页脚广告优先打黑白单面,后期开放双面,此处只需对list按单双面排序即可
			//处理订单详情扩展，仅用户打印
			List<OrderItem> records =new LinkedList<>();
			
			int index = 0;
			for (PrintOrderDetail detail : list) {
				// 详情文档处理中,并且不是清单广告页面
				// if (CommonConstant.ORDER_FIRST_LIST_0 ==
				// detail.getIsFirst().byteValue()) {
				PrintOrderDetail detailAfter=null;
				boolean addAd=false;
				if (detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_SINGLE
						|| detail.getPrintType().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE) {// 黑白,目前只有单,考虑双
					if (hasFooterAd) {
						// 黑白,有页脚广告
						if (CommonConstant.PDF_OPERATE_ING == detail.getStatus().byteValue()) {
							// 需要缩印,并且添加页脚广告
							if(freePageNum>0)
								addAd=true;
							if (freePageNum >= detail.getPageNum()) {
								String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
								OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
								detailAfter=pdfService.pdfMergerAndFooterAd(true, footerAdlist, index, detail.getPageNum(),
										ossObject.getObjectContent(), detail.getReprintType(), detail.getFileName(),
										detail.getOrderDetailId(), detail.getOrderId());
								index += detail.getPageNum();
								freePageNum -= detail.getPageNum();
							} else {
								String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
								OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
								detailAfter=pdfService.pdfMergerAndFooterAd(true, footerAdlist, index, freePageNum,
										ossObject.getObjectContent(), detail.getReprintType(), detail.getFileName(),
										detail.getOrderDetailId(), detail.getOrderId());
								freePageNum -= detail.getPageNum();
							}
						} else {
							// 不需要缩印,只添加页脚广告
							if(freePageNum>0)
								addAd=true;
							if (freePageNum >= detail.getPageNum()) {
								String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
								OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
								detailAfter=pdfService.pdfMergerAndFooterAd(false, footerAdlist, index, detail.getPageNum(),
										ossObject.getObjectContent(), detail.getReprintType(), detail.getFileName(),
										detail.getOrderDetailId(), detail.getOrderId());
								index += detail.getPageNum();
								if(detailAfter.getSourceMD5()==null){
									detailAfter.setSourceMD5(pdf_key.substring(0, pdf_key.lastIndexOf(".pdf")));
								}
								freePageNum -= detail.getPageNum();
							} else {
								String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
								OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
								detailAfter=pdfService.pdfMergerAndFooterAd(false, footerAdlist, index, freePageNum,
										ossObject.getObjectContent(), detail.getReprintType(), detail.getFileName(),
										detail.getOrderDetailId(), detail.getOrderId());
								freePageNum -=freePageNum;
								detailAfter.setSourceMD5(pdf_key.substring(0, pdf_key.lastIndexOf(".pdf")));
							}
						}
					} else {
						if (CommonConstant.PDF_OPERATE_ING == detail.getStatus().byteValue()) {
							// step1 从oss获取文件流
							String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
							OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
							// step2 缩印
							detailAfter=pdfService.pdfMerger(ossObject.getObjectContent(), detail.getReprintType(),
									detail.getFileName(), detail.getOrderDetailId(), detail.getOrderId());
							detailAfter.setSourceMD5(pdf_key.substring(0, pdf_key.lastIndexOf(".pdf")));
						}
					}
				} else {// 彩色
					if (CommonConstant.PDF_OPERATE_ING == detail.getStatus().byteValue()) {
						// step1 从oss获取文件流
						String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("pdf/"));
						OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
						// step2 缩印
						detailAfter=pdfService.pdfMerger(ossObject.getObjectContent(), detail.getReprintType(),
								detail.getFileName(), detail.getOrderDetailId(), detail.getOrderId());
						detailAfter.setSourceMD5(pdf_key.substring(0, pdf_key.lastIndexOf(".pdf")));
					}
				}
				addRecords(detail,records,detailAfter,addAd);
			}
			//添加订单页，因为之前无法获得订单页的详情id所以需要重新查询一次
			OrderCenterDTO orderCenterDTOForItemId = orderCenterService.getOrderByOrderId(orderId, true, false);
			for(PrintOrderDetail detail:orderCenterDTOForItemId.getPrintOrder().getDetails()){
				 if (CommonConstant.ORDER_FIRST_LIST_1 ==
				 detail.getIsFirst().byteValue()) {
					 addRecords(detail,records,null,false);
				 }
			}
			itemService.addBatchOrderItem(records);
			// 成功处理完缩印文档后,更新订单状态
			PrintOrder order = new PrintOrder();
			order.setOrderId(printOrder.getOrderId());
			order.setStatus(PrintConst.ORDER_STATUS_CONFIRMED); // 文档转换完成
			// Result<Boolean> booleanResult = printOrderService.update(order);
			boolean flag = orderCenterService.updateOrder(order);
			if (!flag) {
				logger.error("文档处理完成,更新订单状态为文档转换成功,即status=1,失败!!!");
			}
			// 推送消息
			Thread thread1 = new Thread() {
				public void run() {
					logger.info("*******缩印后异步推送订单信息到店长端");
					orderService.pushToPcConvert(printOrder);
					logger.info("*******缩印后成功推送订单信息到店长端!!!!!!!");
				}
			};
			thread1.start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("哦,low!!!订单异步处理文档失败,数据需要手动修复,订单号:" + orderId + "------>>" + e.getMessage()+";data:"+message.toString());
		}
	}
	/**
	 * 添加广告的订单详情拆分
	 * @param detail
	 * @param list
	 * @param detailAfter
	 * @param addAd
	 */
	public void addRecords(PrintOrderDetail detail,List<OrderItem> list,PrintOrderDetail detailAfter,boolean addAd){
		
		if(addAd){//需要拆分
			OrderItem item=new OrderItem();
			item.setIsFirst(detail.getIsFirst());
			item.setName(detail.getFileName());
			item.setOrderId(detail.getOrderId());
			item.setOrderItemId(detail.getOrderDetailId());
			item.setPageNum(detail.getPageNum());
			item.setPdfMd5(detailAfter.getPdfMD5()+".pdf");
			item.setPrintType(detail.getPrintType());
			item.setReprintType(detail.getReprintType());
			item.setQuantity(1);
			if(detail.getNum()-1>0){
				item.setHasAd((byte)1);
			}else{
				item.setHasAd((byte)0);
			}
			list.add(item);
			if(detail.getNum()-1>0){
				OrderItem item2=new OrderItem();
				item2.setIsFirst(detail.getIsFirst());
				item2.setName(detail.getFileName());
				item2.setOrderId(detail.getOrderId());
				item2.setOrderItemId(detail.getOrderDetailId());
				item2.setPageNum(detail.getPageNum());
				item2.setPdfMd5(detailAfter.getSourceMD5()+".pdf");
				item2.setPrintType(detail.getPrintType());
				item2.setReprintType(detail.getReprintType());
				item2.setQuantity(detail.getNum()-1);
				item2.setHasAd((byte)0);
				list.add(item2);
			}
		}else{//不需要拆分
			OrderItem item=new OrderItem();
			item.setIsFirst(detail.getIsFirst());
			item.setName(detail.getFileName());
			item.setOrderId(detail.getOrderId());
			item.setOrderItemId(detail.getOrderDetailId());
			item.setPageNum(detail.getPageNum());
			if(detailAfter!=null){
				item.setPdfMd5(detailAfter.getPdfMD5()+".pdf");
			}else{
				item.setPdfMd5(detail.getPdfMD5());
			}
			item.setPrintType(detail.getPrintType());
			item.setReprintType(detail.getReprintType());
			item.setQuantity(detail.getNum());
			item.setHasAd((byte)0);
			list.add(item);
		}
	}
}