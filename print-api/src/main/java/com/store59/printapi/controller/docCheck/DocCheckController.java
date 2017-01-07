package com.store59.printapi.controller.docCheck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.model.PrintOrderDetail;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.service.docCheck.DocCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/7/20
 * @since 1.0
 */
@RestController
@RequestMapping("/doccheck/*")
public class DocCheckController {
    @Autowired
    private DocCheckService docCheckService;

    @RequestMapping(value = "/items/list", method = RequestMethod.GET)
    public Object getUncheckOrderItems(String key) {
        if (!"e1397869ef9816669af1a9312144fec3".equals(key)) {
            return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "非法用户!");
        }
        return DatagramHelper.getDatagramWithSuccess(docCheckService.getUncheckOrderItems());
    }

    @RequestMapping(value = "/orderIds/update", method = RequestMethod.POST)
    public Object updateUncheckOrderIds(String key, String orderIds) {
        if (!"e1397869ef9816669af1a9312144fec3".equals(key)) {
            return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "非法用户!");
        }
        if (StringUtil.isBlank(orderIds)) {
            return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "订单号不能为空!");
        }
//        List<String> idList = Arrays.asList(orderIds.split(","));
        List<String> idList = new ArrayList<>();
        idList = JsonUtil.getObjectFromJson(orderIds, new TypeReference<List<String>>() {
        });
        boolean flag = docCheckService.updateOrderIdsStatus(idList);
        return DatagramHelper.getDatagramWithSuccess(flag);
    }

    //TODO 更新商品详情检测状态
    @RequestMapping(value = "/items/update", method = RequestMethod.POST)
    public Object updateItemCheckStatus(String key, String details) {
        if (!"e1397869ef9816669af1a9312144fec3".equals(key)) {
            return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "非法用户!");
        }
        if (StringUtil.isBlank(details)) {
            return DatagramHelper.getDatagram(CommonConstant.GLOBAL_STATUS_ERROR, "商品列表不能为空!");
        }
        List<PrintOrderDetail> list = new ArrayList<>();
        try {
            list = JsonUtil.getObjectFromJson(details, new TypeReference<List<PrintOrderDetail>>() {
            });
        } catch (Exception e) {
            System.out.println("*******Details:" + details);
            throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
        }

        docCheckService.updateItemCheckStatus(list);
        return DatagramHelper.getDatagramWithSuccess();
    }
}