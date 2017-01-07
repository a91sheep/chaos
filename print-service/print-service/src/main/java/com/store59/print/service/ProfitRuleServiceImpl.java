/**
 * Copyright (c) 2015, 59store. All rights reserved. 
 */
package com.store59.print.service;

import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.print.common.remoting.ProfitRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.store59.print.dao.ProfitRuleDao;
import com.store59.print.common.model.ProfitRule;

/**
 * ProfitRule RPC服务实现层
 * 
 * @author <a href="mailto:zhongc@59store.com">士兵</a> 
 * 16/02/27
 * @since 1.0
 */
@RemoteService(serviceType = ServiceType.HESSIAN, serviceInterface = ProfitRuleService.class, exportPath = "/profitrule")
public class ProfitRuleServiceImpl implements ProfitRuleService {

    @Autowired
    private ProfitRuleDao profitRuleDao;

    @Override
    public Result<ProfitRule> findByDormId(Integer dormId, Byte type) {
        return ResultHelper.genResultWithSuccess(profitRuleDao.findByDormId(dormId, type));
    }

    @Override
    public Result<ProfitRule> insert(ProfitRule profitRule){
        return ResultHelper.genResultWithSuccess(profitRuleDao.insert(profitRule) == 1 ? profitRule : null);
    }

    @Override
    public Result<Boolean> update(ProfitRule profitRule){
        return ResultHelper.genResultWithSuccess(profitRuleDao.update(profitRule) == 1 ? true : false);
    }

}