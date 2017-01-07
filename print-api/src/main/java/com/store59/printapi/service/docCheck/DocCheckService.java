package com.store59.printapi.service.docCheck;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.order.common.service.dto.OrderItemDTO;
import com.store59.order.common.service.dto.OrderItemUpdate;
import com.store59.order.common.service.facade.OrderUpdateFacade;
import com.store59.order.common.service.facade.SellerOrderQueryFacade;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.print.common.remoting.PrintOrderRecordService;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.service.OrderCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
@Service
public class DocCheckService {
    @Autowired
    private PrintOrderRecordService printOrderRecordService;
    @Autowired
    private SellerOrderQueryFacade  sellerOrderQueryFacade;
    @Autowired
    private OrderCenterService      orderCenterService;
    @Autowired
    private OrderUpdateFacade       orderUpdateFacade;

    private Logger logger = LoggerFactory.getLogger(DocCheckService.class);

    public List<PrintOrderDetail> getUncheckOrderItems() {
        List<PrintOrderDetail> detailList = new ArrayList<>();
        Result<List<String>> result = printOrderRecordService.getUncheckOrderIds();
        if (result == null || result.getStatus() != 0 || result.getData() == null) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (result.getData().size() == 0) {
            return null;
        }
        List<String> orderIds = result.getData();

        // 获取订单商品列表
        Result<List<OrderItemDTO>> orderItemsRet = sellerOrderQueryFacade.queryOrderItems(orderIds);
        if (orderItemsRet == null || orderItemsRet.getData() == null || result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        List<OrderItemDTO> itemDTOList = orderItemsRet.getData();
        if (itemDTOList.size() == 0) {
            return null;
        }
        // 转换成printOrderDetail返回给文档检测程序
        for (OrderItemDTO itemDTO : itemDTOList) {
            PrintOrderDetail detail = orderCenterService.converOrderItemToPrintDetail(itemDTO);
            if (detail.getIsFirst().byteValue() == CommonConstant.ORDER_FIRST_LIST_0) {
                detailList.add(detail);
            }
        }

        return detailList;
    }

    public boolean updateOrderIdsStatus(List<String> orderIds) {
        logger.info("******updateOrderIdsStatus:" + JsonUtil.getJsonFromObject(orderIds));
        Result<Boolean> result = printOrderRecordService.updateRecordStatus(orderIds);
        logger.info("******更新结果<" + orderIds.get(0) + ">:" + JsonUtil.getJsonFromObject(result));
        if (result == null || result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        return true;
    }

    public boolean updateItemCheckStatus(List<PrintOrderDetail> detailList) {
        logger.info("updateItemCheckStatus参数:" + JsonUtil.getJsonFromObject(detailList));
        List<OrderItemUpdate> orderItemUpdateList = new ArrayList<>();
        for (PrintOrderDetail detail : detailList) {
            OrderItemDTO dto = orderCenterService.converPrintDetailToOrderItem(detail);
            OrderItemUpdate orderItemUpdate = new OrderItemUpdate();
            orderItemUpdate.setId(dto.getId());
            orderItemUpdate.setOrderId(dto.getOrderId());
            orderItemUpdate.setExtension(dto.getExtension());
            orderItemUpdateList.add(orderItemUpdate);
        }
        Result<?> result = orderUpdateFacade.updateOrderItems(orderItemUpdateList);
        logger.info("******更新结果<" + detailList.get(0).getOrderDetailId() + ">:" + JsonUtil.getJsonFromObject(result));
        if (result == null || result.getStatus() != 0) {
            logger.error("订单商品更新失败,商品信息:" + JsonUtil.getJsonFromObject(detailList));
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        return true;
    }
}
