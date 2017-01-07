package com.yunyin.print.main.api.common.message;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.yunyin.print.common.constant.PrintCommonConstant;
import com.yunyin.print.common.enums.OrderStatusEnum;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;
import com.yunyin.print.common.remoting.PrintServiceClient;
import com.yunyin.print.main.api.common.utils.FileUtil;
import com.yunyin.print.main.api.service.common.message.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/23
 * @since 1.0
 */
@Component
public class DocReveiver {
    private Logger logger = LoggerFactory.getLogger(DocReveiver.class);
    @Autowired
    private PdfService         pdfService;
    @Autowired
    private PrintServiceClient printServiceClient;

    @Autowired
    private OSSClient ossClient;
    @Value("${aliyun.oss.print.bucketName}")
    private String    bucketName;

    public void receive(Object message) {
        logger.info("开始异步处理订单文档:" + message.toString());

        Map<String, Object> msg = JsonUtil.getObjectFromJson((String) message, Map.class);
        String orderId = (String) msg.get("orderId");

        //获取订单信息
        Result<PrintOrder> result = printServiceClient.getOrderByOrderId(orderId, true);
        PrintOrder printOrder = result.getData();

        try {
            //step1 create first_page
            pdfService.createFirstSign(printOrder);

            //step2 convert
            for (PrintOrderDetail detail : printOrder.getDetails()) {
                //筛选需要处理的文档
                if (detail.getPrintStatus().byteValue() == PrintCommonConstant.DOC_DEALING) {
                    String pdfKey = detail.getPdfUrl().substring(detail.getPdfUrl().lastIndexOf("pdf/"));
                    OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdfKey);
                    pdfService.pdfMerger(detail, ossObject.getObjectContent(), detail.getMinimoType(), detail.getFileName());
                }
            }
            //step3 更新订单状态
            PrintOrder order = new PrintOrder();
            order.setOrderId(printOrder.getOrderId());
            order.setOrderStatus(OrderStatusEnum.CONFIRMED.getCode()); // 文档转换完成
            Result<Boolean> orderRet = printServiceClient.update(order);
            if (orderRet == null || !orderRet.getData()) {
                logger.error("文档处理完成,更新订单状态失败（ 0 -－>> 1 ）");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("哦,low!!!订单异步处理文档失败,数据需要手动修复,订单号:" + orderId + "------>>" + e.getMessage() + ";data:" + message.toString());
        }
    }
}
