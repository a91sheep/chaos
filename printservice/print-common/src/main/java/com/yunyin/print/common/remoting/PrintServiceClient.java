package com.yunyin.print.common.remoting;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.yunyin.print.common.filter.PrintOrderFilter;
import com.yunyin.print.common.model.PSitePrice;
import com.yunyin.print.common.model.PTerminal;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
@FeignClient("print-service")
public interface PrintServiceClient {

    //p_order
    @RequestMapping(value = "/printservice/order/record", method = RequestMethod.GET)
    Result<PrintOrder> getOrderByOrderId(String orderId, Boolean withDetail);

    @RequestMapping(value = "/printservice/order/printCode", method = RequestMethod.GET)
    Result<PrintOrder> getOrderByPrintCode(String printCode, Boolean withDetail);

    @RequestMapping(value = "/printservice/order/findByFilter", method = RequestMethod.GET)
    Result<List<PrintOrder>> getOrderByFilter(PrintOrderFilter filter, Boolean withDetail);

    @RequestMapping(value = "/printservice/order/insert", method = RequestMethod.POST)
    Result<PrintOrder> insert(PrintOrder printOrder);

    @RequestMapping(value = "/printservice/order/update", method = RequestMethod.POST)
    Result<Boolean> update(PrintOrder printOrder);

    //p_order_detail
    @RequestMapping(value = "/printservice/orderDetail/addBatch", method = RequestMethod.POST)
    Result<Boolean> addBatch(String orderId, List<PrintOrderDetail> details);

    @RequestMapping(value = "/printservice/orderDetail/update", method = RequestMethod.POST)
    Result<Boolean> update(PrintOrderDetail detail);

    //p_terminal
    @RequestMapping(value = "/printservice/terminal/list", method = RequestMethod.GET)
    Result<List<PTerminal>> getTerminalListBySiteId(Long siteId);

    //p_site_price
    @RequestMapping(value = "/printservice/price/list", method = RequestMethod.GET)
    Result<List<PSitePrice>> getPriceList(Long siteId);
}