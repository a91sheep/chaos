package com.store59.printapi.controller.wechat;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.remoting.PrintOrderService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.BASE64DecodedMultipartFile;
import com.store59.printapi.common.utils.FileUtil;
import com.store59.printapi.common.utils.IamgesResize;
import com.store59.printapi.service.createOrder.AppCreateOrderService;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年6月5日 下午5:50:45 类说明
 */
@RestController
@RequestMapping("pic")
public class PicController {
    @Autowired
    private PrintOrderService printOrderService;

    @Autowired
    private OSSClient      ossClient;
    @Value("${rabbitmq.event.print.entry}")
    private String         queueName;
    @Value("${aliyun.oss.print.bucketName}")
    private String         bucketName;
    @Value("${aliyun.oss.print.domainName}")
    private String         domainName;
    @Value("${store59.first.page.ad}")
    private String         store59firstPageAdUrl;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(PicController.class);

    @RequestMapping("test")
    public void receive(@RequestParam Long orderId) {
        // logger.info("照片消息处理订单照片:" + message.toString());
        // JSONObject msg = JSONObject.parseObject(message);
        // Long orderId = 0L;
        // if (msg.get("orderId") != null)
        // orderId = Long.parseLong(msg.get("orderId").toString());
        // if (orderId == 0) {
        // logger.info("照片转换未获取到orderId" + message);
        // return;
        // }
        try {
            Result<PrintOrder> result = printOrderService.findByOrderId(orderId, true);
            if (result == null || result.getStatus() != 0) {
                logger.error("照片消息处理,获取订单信息失败!!!订单号:" + orderId);
                throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR, "消息处理照片,获取订单信息失败!!!订单号:" + orderId);

            }
            PrintOrder printOrder = result.getData();
            for (PrintOrderDetail detail : printOrder.getDetails()) {
                String pdf_key = detail.getUrl().substring(detail.getUrl().lastIndexOf("/") + 1);
                OSSObject ossObject = FileUtil.getOssObject(ossClient, bucketName, pdf_key);
                byte[] rize = IamgesResize.getPic(ossObject.getObjectContent(), 297, 420);
                BASE64DecodedMultipartFile multipartFile = new BASE64DecodedMultipartFile(rize);
                // ---------缩印文件保存---------
                multipartFile.getContentType();
                String conver_key = FileUtil.upload(multipartFile, "image/jpeg", "a.jpeg", ossClient, bucketName);
                picUpdate(conver_key, detail.getOrderDetailId(), orderId);
            }
            // 成功处理完照片照片后,更新订单状态
            PrintOrder order = new PrintOrder();
            order.setOrderId(printOrder.getOrderId());
            order.setStatus(PrintConst.ORDER_STATUS_CONFIRMED);
            Result<Boolean> booleanResult = printOrderService.update(order);
            if (booleanResult == null || !booleanResult.getData()) {
                logger.error("照片处理完成,更新订单状态为照片转换成功,即status=1,失败!!!");
            }
        } catch (Exception e) {
            logger.error("订单异步处理照片失败,数据需要手动修复,订单号:" + orderId + "------>>" + e.getMessage());
        }
    }

    public void picUpdate(String conver_key, Long orderDetailId, Long orderId) {
        PrintOrderDetail detail = new PrintOrderDetail();
        detail.setOrderDetailId(orderDetailId);
        try {
            // ---------缩印文件保存---------
            logger.info("orderDetailId:" + orderDetailId + "----->缩印文件保存成功,conver_key:" + conver_key);
            String pdf_path = domainName + "/" + conver_key;
            String pdf_md5 = conver_key.substring(0, conver_key.lastIndexOf("."));
            // 此处只要更新缩印后的pdf信息即可
            detail.setUrl(pdf_path);
            detail.setPdfMD5(pdf_md5);
            // 文档处理完成
            detail.setStatus(CommonConstant.PDF_OPERATE_FINISH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Result<Boolean> result = printOrderService.updatePrintOrderDetail(detail);
        if (result == null || !result.getData()) {
            logger.error("缩印后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
            throw new BaseException(CommonConstant.GLOBAL_STATUS_ERROR,
                    "缩印后更新订单详情失败,订单详情号:" + orderDetailId + ",订单号:" + orderId);
        }
    }

    @RequestMapping("pdf")
    public void testpdfconvert(String orderId) {
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>(); // 拼接消息队列--文档处理信息
        paramMap.put("freePageNum", 0);// 消息消费者如果接收的freePageNum==0,说明没有广告
        paramMap.put("firstPageUrl", store59firstPageAdUrl);// 如果没有选择广告打印,则首页选择59的广告页
        paramMap.put("orderId", Long.parseLong(orderId));
        String pushPcString = JsonUtil.getJsonFromObject(paramMap);
        logger.info("消息队列处理订单文档缩印,{}", pushPcString);
        rabbitTemplate.convertAndSend(queueName, pushPcString);
    }
}