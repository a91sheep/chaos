package com.store59.printapi.service.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.lowagie.text.DocumentException;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.order.common.service.dto.OrderItemDTO;
import com.store59.order.common.service.dto.OrderItemUpdate;
import com.store59.order.common.service.facade.OrderUpdateFacade;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.PDFUtil;
import com.store59.printapi.model.result.ad.AdFooterMsg;
import com.store59.printapi.service.OrderCenterService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/19
 * @since 1.0
 */
@Service
public class PdfService {
//    @Autowired
//    private PrintOrderService printOrderService;

    private static Logger logger = LoggerFactory.getLogger(PdfService.class);
    @Autowired
    private OSSClient          ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String             bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String             domainName;
    @Value("${first.default.image.logo}")
    private String             imageLogoUrl;
    @Value("${first.del.image.yourself}")
    private String             imageYourselfUrl;
    @Value("${first.del.image.dormer}")
    private String             imageDormerUrl;
    @Autowired
    private OrderCenterService orderCenterService;
    @Autowired
    private OrderUpdateFacade  orderUpdateFacade;

    public void createFirstSign(PrintOrder printOrder, String firstPageUrl) {
        byte[] river = new byte[0];
        try {
            if (printOrder.getDeliveryType().byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
                river = PDFUtil.createPdf(printOrder, firstPageUrl, imageDormerUrl, imageLogoUrl);
            } else {
                river = PDFUtil.createPdf(printOrder, firstPageUrl, imageYourselfUrl, imageLogoUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(river);
        String conver_key = FileUtil.upload(multipartFile, "application/pdf", "打印清单广告页.pdf", ossClient, bucketName);
        String pdf_path = domainName + "/" + conver_key;
        String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));

        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderId(printOrder.getOrderId());
        detail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
        detail.setIsFirst(CommonConstant.ORDER_FIRST_LIST_1);
        detail.setUrl(pdf_path);
        detail.setPdfMD5(pdf_md5);
        detail.setSourceUrl(pdf_path);
        detail.setSourceMD5(pdf_md5);
        detail.setFileName(printOrder.getOrderId() + ".pdf");
        detail.setPageNum(1);
        detail.setNum(1);
        //非空字段设值
        detail.setPrintType((byte) 1);
        detail.setReprintType((byte) 0);
        detail.setAmount(0f);
        detail.setCheckStatus(CommonConstant.CHECK_STATUS_2);  //首页广告直接设置检测失败,不参与返利
//        Result<PrintOrderDetail> result = printOrderService.insertPrintOrderDetail(printOrder.getOrderId(), detail);
        OrderItemDTO dto = orderCenterService.converPrintDetailToOrderItem(detail);
        List<OrderItemDTO> li = new ArrayList<>();
        li.add(dto);
        Result<?> result = orderUpdateFacade.saveOrderItems(li);
        if (result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
    }

    public PrintOrderDetail pdfMerger(InputStream inputStream, byte reprintType, String sourFileName, Long orderDetailId, String orderId) {
        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderDetailId(orderDetailId);
        //上传前判断
        int w = 1;  //缩印变量
        int h = 1;  //缩印变量
        if (PrintConst.re_print_type_two == reprintType) {
            w = 2;
            h = 1;
        } else if (PrintConst.re_print_type_four == reprintType) {
            w = 2;
            h = 2;
        } else if (PrintConst.re_print_type_six == reprintType) {
            w = 3;
            h = 2;
        } else if (PrintConst.re_print_type_nine == reprintType) {
            w = 3;
            h = 3;
        }
        try {
            byte[] river = new byte[0];
            try {
                river = PDFUtil.manipulatePdf(inputStream, w, h);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sourFileName.split("\\.").length == 1) {
                sourFileName = sourFileName + ".pdf";
            } else {
                sourFileName = sourFileName.split("\\.")[0] + ".pdf";
            }
            BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(river);
            //---------缩印文件保存---------
            String conver_key = FileUtil.upload(multipartFile, "application/pdf", sourFileName, ossClient, bucketName);
            logger.info("orderDetailId:" + orderDetailId + "----->缩印文件保存成功,conver_key:" + conver_key);
            String pdf_path = domainName + "/" + conver_key;
            String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));
            //此处只要更新缩印后的pdf信息即可
            detail.setUrl(pdf_path);
            detail.setPdfMD5(pdf_md5);
            //文档处理完成
            detail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

//        Result<Boolean> result = printOrderService.updatePrintOrderDetail(detail);
        OrderItemDTO dto = orderCenterService.converPrintDetailToOrderItem(detail);
        List<OrderItemDTO> li = new ArrayList<>();
        li.add(dto);
        OrderItemUpdate orderItemUpdate = new OrderItemUpdate();
        orderItemUpdate.setId(dto.getId());
        orderItemUpdate.setExtension(dto.getExtension());
        orderItemUpdate.setOrderId(orderId);
        Result<?> result = orderUpdateFacade.updateOrderItem(orderItemUpdate);
        if (result == null || result.getStatus() != 0) {
            logger.error("缩印后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
            throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "缩印后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
        }
        return detail;
    }

    /**
     * @param needScale     是否需要缩印
     * @param footerAdlist
     * @param index         footerAdlist的index
     * @param num           使用index开始的几个广告
     * @param inputStream
     * @param reprintType
     * @param sourFileName
     * @param orderDetailId
     * @param orderId
     */
    public PrintOrderDetail pdfMergerAndFooterAd(boolean needScale, List<AdFooterMsg> footerAdlist, int index, int num, InputStream inputStream, byte reprintType, String sourFileName, Long orderDetailId, String orderId) {
        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderDetailId(orderDetailId);
        //上传前判断
        int w = 1;  //缩印变量
        int h = 1;  //缩印变量
        if (PrintConst.re_print_type_two == reprintType) {
            w = 2;
            h = 1;
        } else if (PrintConst.re_print_type_four == reprintType) {
            w = 2;
            h = 2;
        } else if (PrintConst.re_print_type_six == reprintType) {
            w = 3;
            h = 2;
        } else if (PrintConst.re_print_type_nine == reprintType) {
            w = 3;
            h = 3;
        }
        String merage_md5=null;
        try {
            byte[] river = new byte[0];
            try {
                if (needScale) {//缩印后,添加页脚广告
                    BASE64DecodedMultipartFile file = new BASE64DecodedMultipartFile(PDFUtil.manipulatePdf(inputStream, w, h));
                    merage_md5=mergePdfUpload(sourFileName,file);//返回缩印后未添加广告页的文档
                    river = PDFUtil.headerAndFooter(file.getInputStream(), footerAdlist, index, num);
                    logger.info("##########orderId:{},缩印后添加页脚广告,orderDetailId:{}-->添加张数:{}", orderId, orderDetailId, num);
                } else {
                    river = PDFUtil.headerAndFooter(inputStream, footerAdlist, index, num);
                    logger.info("##########orderId:{},直接添加页脚广告,orderDetailId:{}-->添加张数:{}", orderId, orderDetailId, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(river);
            if (sourFileName.split("\\.").length == 1) {
                sourFileName = sourFileName + ".pdf";
            } else {
                sourFileName = sourFileName.split("\\.")[0] + ".pdf";
            }
            //---------文件保存---------
            String conver_key = FileUtil.upload(multipartFile, "application/pdf", sourFileName, ossClient, bucketName);
            String pdf_path = domainName + "/" + conver_key;
            String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));
            detail.setUrl(pdf_path);
            detail.setPdfMD5(pdf_md5);
            //文档处理完成
            detail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

//        Result<Boolean> result = printOrderService.updatePrintOrderDetail(detail);
        OrderItemDTO dto = orderCenterService.converPrintDetailToOrderItem(detail);
        List<OrderItemDTO> li = new ArrayList<>();
        li.add(dto);
        OrderItemUpdate orderItemUpdate = new OrderItemUpdate();
        orderItemUpdate.setId(dto.getId());
        orderItemUpdate.setExtension(dto.getExtension());
        orderItemUpdate.setOrderId(orderId);
        Result<?> result = orderUpdateFacade.updateOrderItem(orderItemUpdate);
        if (result == null || result.getStatus() != 0) {
            logger.error("添加页脚广告后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
            throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "添加页脚广告后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
        }
        detail.setSourceMD5(merage_md5);
        return detail;
    }
    private String mergePdfUpload(String sourFileName,BASE64DecodedMultipartFile multipartFile){
        if (sourFileName.split("\\.").length == 1) {
            sourFileName = sourFileName + ".pdf";
        } else {
            sourFileName = sourFileName.split("\\.")[0] + ".pdf";
        }
        //---------文件保存---------
        String conver_key = FileUtil.upload(multipartFile, "application/pdf", sourFileName, ossClient, bucketName);
        String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));
        return pdf_md5;
    }
}