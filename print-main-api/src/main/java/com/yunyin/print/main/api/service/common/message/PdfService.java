package com.yunyin.print.main.api.service.common.message;

import com.aliyun.oss.OSSClient;
import com.lowagie.text.DocumentException;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.yunyin.print.common.constant.PrintCommonConstant;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.common.remoting.PrintServiceClient;
import com.yunyin.print.main.api.common.constant.CommonConstant;
import com.yunyin.print.main.api.common.utils.BASE64DecodedMultipartFile;
import com.yunyin.print.main.api.common.utils.FileUtil;
import com.yunyin.print.main.api.common.utils.PDFUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/23
 * @since 1.0
 */
@Service
public class PdfService {
    private static Logger logger = LoggerFactory.getLogger(PdfService.class);
    @Autowired
    private OSSClient          ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String             bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String             domainName;
    @Autowired
    private PrintServiceClient printServiceClient;

    public void createFirstSign(PrintOrder printOrder) {
        byte[] river = new byte[0];
//        try {
//            if (printOrder.getDeliveryType().byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
//                river = PDFUtil.createPdf(printOrder, firstPageUrl, imageDormerUrl, imageLogoUrl);
//            } else {
//                river = PDFUtil.createPdf(printOrder, firstPageUrl, imageYourselfUrl, imageLogoUrl);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
        BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(river);
        String conver_key = FileUtil.upload(multipartFile, "application/pdf", "打印清单广告页.pdf", ossClient, bucketName);
        String pdf_path = domainName + "/" + conver_key;
        String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));

        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderId(printOrder.getOrderId());
        detail.setPrintStatus(PrintCommonConstant.DOC_DEAL_FINISHED);
        detail.setIsFirst(PrintCommonConstant.IS_FIRST_YES);
        detail.setPdfUrl(pdf_path);
        detail.setPdfMD5(pdf_md5);
        detail.setSourceUrl(pdf_path);
        detail.setSourceMD5(pdf_md5);
        detail.setFileName(printOrder.getOrderId() + ".pdf");
        detail.setPdfNum(1);
        detail.setPrintNum(1);
        //非空字段设值
        detail.setPrintType(PrintCommonConstant.PRINT_TYPE_BLACK_WHITE);
        detail.setMinimoType(PrintCommonConstant.MINIMO_TYPE_NO);
        detail.setPaperType(PrintCommonConstant.PAPER_TYPE_A4);
        detail.setPrintSide(PrintCommonConstant.PRINT_SIDE_SINGLE);
        detail.setDocType(PrintCommonConstant.DOC_TYPE_DOCUMENT);
        detail.setAmount(0f);

        List<PrintOrderDetail> detailList = new ArrayList<>();
        detailList.add(detail);
        Result<Boolean> result = printServiceClient.addBatch(printOrder.getOrderId(), detailList);
        if (result == null || !result.getData()) {
            throw new ServiceException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION + "--add first page fail,orderId=" + printOrder.getOrderId());
        }
    }

    public void pdfMerger(PrintOrderDetail detail, InputStream inputStream, byte reprintType, String sourFileName) {
        //上传前判断
        int w = 1;  //缩印变量
        int h = 1;  //缩印变量
        if (PrintCommonConstant.MINIMO_TYPE_TWO == reprintType) {
            w = 2;
            h = 1;
        } else if (PrintCommonConstant.MINIMO_TYPE_FOUR == reprintType) {
            w = 2;
            h = 2;
        }

        if (sourFileName.split("\\.").length == 1) {
            sourFileName = sourFileName + ".pdf";
        } else {
            sourFileName = sourFileName.split("\\.")[0] + ".pdf";
        }
        try {
            BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(PDFUtil.manipulatePdf(inputStream, w, h));
            //---------文件保存---------
            String converKey = FileUtil.upload(multipartFile, "application/pdf", sourFileName, ossClient, bucketName);
            String pdfMd5 = converKey.substring(0, converKey.lastIndexOf("."));

            PrintOrderDetail detail_new = new PrintOrderDetail();
            detail_new.setId(detail.getId());
            detail_new.setPdfMD5(pdfMd5);
            detail_new.setPdfUrl(converKey);

            Result<Boolean> result = printServiceClient.update(detail_new);
            if (result == null || !result.getData()) {
                throw new ServiceException(CommonConstant.GLOBAL_STATUS_ERROR, "缩印后订单详情更新失败！！，detailId=" + detail.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
