/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.print.common.remoting;

import com.store59.print.common.model.ProfitRule;
import com.store59.kylin.common.model.Result;

/**
 * ProfitRule RPC服务接口层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 16/02/27
 * @since 1.0
 */
public interface ProfitRuleService {

    /**
     * 查询店长的返利规则
     *
     * @param dormId
     * @param type
     * @return
     */
    Result<ProfitRule> findByDormId(Integer dormId, Byte type);

    /**
     * 添加返利规则
     *
     * @param profitRule
     * @return
     */
    Result<ProfitRule> insert(ProfitRule profitRule);

    /**
     * 修改返利规则
     *
     * @param profitRule
     * @return
     */
    Result<Boolean> update(ProfitRule profitRule);

}