/**
 *
 */
package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.PrintOrder;
import com.store59.print.common.model.PrintOrderDetail;

/**
 * 打印店订单接口
 *
 * @author <a href="mailto:huangh@59store.com">亦橙</a>
 * @version 1.1 2015年12月25日
 * @since 1.1
 */
public interface PrintOrderService {
    /**
     * 获取订单信息
     *
     * @param orderId  订单id
     * @param isDetail 是否获取订单详情
     * @return
     */
    Result<PrintOrder> findByOrderId(Long orderId, Boolean isDetail);

    /**
     * 组合条件查询订单信息
     * dormId和uid不能同时为空
     *
     * @param filter   条件选择器
     * @param isDetail 是否获取订单详情
     * @return
     */
    Result<List<PrintOrder>> findByFilter(PrintOrderFilter filter, Boolean isDetail);

    /**
     * 统计订单个数
     * dormId和uid不能同时为空
     *
     * @param filter 条件选择器
     * @return
     */
    Result<Integer> findCountByFilter(PrintOrderFilter filter);

    /**
     * 统计订单总金额
     * dormId和uid不能同时为空
     *
     * @param filter 条件选择器
     * @return
     */
    Result<Double> findSumAmountByFilter(PrintOrderFilter filter);

    /**
     * 添加订单
     *
     * @param printOrder
     * @return
     */
    Result<PrintOrder> insert(PrintOrder printOrder);

    /**
     * 修改订单信息
     *
     * @param printOrder
     * @return
     */
    Result<Boolean> update(PrintOrder printOrder);

    /**
     * 更新返利结果
     *
     * @return
     */
    Result<Boolean> updateProfit(int detailId, byte isProfit);

    /**
     * 插入一个订单详情
     */
    Result<PrintOrderDetail> insertPrintOrderDetail(Long orderId, PrintOrderDetail printOrderDetail);

    /**
     * 更新订单详情
     */
    Result<Boolean> updatePrintOrderDetail(PrintOrderDetail printOrderDetail);
}
