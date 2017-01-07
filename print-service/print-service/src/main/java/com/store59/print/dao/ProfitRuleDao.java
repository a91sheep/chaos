/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.print.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.print.common.model.ProfitRule;
import com.store59.print.dao.mapper.ProfitRuleMapper;

/**
 * ProfitRule 数据访问层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 16/02/27
 * @since 1.0
 */
@Repository
public class ProfitRuleDao {

    @Autowired
    private ProfitRuleMapper masterProfitRuleMapper;

    @Autowired
    private ProfitRuleMapper slaveProfitRuleMapper;

    public int insert(ProfitRule profitRule){
        return masterProfitRuleMapper.insert(profitRule);
    }

    public int update(ProfitRule profitRule){
        return masterProfitRuleMapper.update(profitRule);
    }

    public ProfitRule findByDormId(Integer dormId, Byte type) {
        return slaveProfitRuleMapper.findByDormId(dormId, type);
    }
}