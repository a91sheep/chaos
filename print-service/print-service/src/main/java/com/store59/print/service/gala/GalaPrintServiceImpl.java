package com.store59.print.service.gala;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.store59.kylin.utils.StringUtil;
import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.gala.GalaPrintRelation;
import com.store59.print.common.remoting.GalaPrintService;
import com.store59.print.dao.GalaPrintRelationDao;
import com.store59.rpc.utils.server.annotation.RemoteService;
import com.store59.rpc.utils.server.annotation.ServiceType;

/**
 * 59打印店活动,数据实现层
 *
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.1 2016年4月19日
 * @since 1.1
 */
@RemoteService(serviceInterface = GalaPrintService.class, serviceType = ServiceType.HESSIAN, exportPath = "/printgala")
public class GalaPrintServiceImpl implements GalaPrintService {

	private static Logger logger = LoggerFactory.getLogger(GalaPrintService.class);
	
	@Autowired
	private GalaPrintRelationDao galaPrintRelationDao;
	
	@Override
	public Result<GalaPrintRelation> insert(GalaPrintRelation printRelation) {
		return ResultHelper.genResultWithSuccess(galaPrintRelationDao.insert(printRelation) == 1 ? printRelation : null);
	}

	@Override
	public Result<Integer> findGalaAmountByFilter(PrintOrderFilter filter) {
        if (filter.getDormId() == null && StringUtil.isEmpty(filter.getPhone())) {
            throw new ServiceException(4, "dormId or phone must exists one");
        }
       
        return ResultHelper.genResultWithSuccess(galaPrintRelationDao.findGalaAmountByFilter(filter));
	}

}
