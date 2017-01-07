/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.print.dao.mapper;

import com.store59.print.common.model.ProfitRule;
import org.apache.ibatis.annotations.Param;

/**
 * ProfitRule 数据持久层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 16/02/27
 * @since 1.0
 */
public interface ProfitRuleMapper {

    int insert(ProfitRule profitRule);

    int update(ProfitRule profitRule);

    ProfitRule findByDormId(@Param("dormId") Integer dormId, @Param("type") Byte type);
}