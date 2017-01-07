/**
 * 
 */
package com.store59.print.common.remoting;

import java.util.List;

import com.store59.kylin.common.model.Result;
import com.store59.print.common.filter.Print259UserFilter;
import com.store59.print.common.model.gala259user.Print259Record;
import com.store59.print.common.model.gala259user.Print259Repay;
import com.store59.print.common.model.gala259user.Print259User;
import com.store59.print.common.model.gala259user.PrintShopTime;

/**
 * 打印店259新用户接口
 * 
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午3:00:29
 * @since 1.0
 */
public interface Print259UserService {
    /**
     * 组合条件查询259新用户
     * @param filter   条件选择器
     * @return
     */
    Result<List<Print259User>> findByFilter(Print259UserFilter filter);
    
    /**
     * 统计店长已完成的259用户数
     * @param filter
     * @return
     */
    Result<Integer> findCountByFilter(Print259UserFilter filter);
    
    /**
     * 查询店长259活动的返款计划
     * @param dormId
     * @return
     */
    Result<List<Print259Repay>> findRepayByDormId(Integer dormId);
    
    /**
     * 查询店长是否完成259个新用户
     * @param dormId
     * @return
     */
    Result<Print259Record> findDormFinish259Time(Integer dormId);
    
    /**
     * 查询店长某个月的营业时间
     * @param dormId
     * @return
     */
    Result<List<PrintShopTime>> findPrintShopTimeByDormId(Integer dormId,Long time);
    
    /**
     * 查询店长某个月的营业时间
     * @param dormId
     * @return
     */
    Result<Boolean> findCountPrintShopTimeByDormId(Integer dormId,Long time);
    
    /**
     * 修改店长259活动完成时间记录表的返款状态值为0-失效或全部返款完成
     * @param dormId
     * @return
     */
    Result<Boolean> update259RecordValidValue(Integer dormId,Byte status);
    
    /**
     * 批量修改返款记录
     * @param repaylist
     * @return
     */
    Result<Boolean> updateBatchRepayList(List<Print259Repay> repaylist);
    

}
