package com.yunyin.print.service.controller;

import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.common.utils.ResultHelper;
import com.yunyin.print.common.model.PTerminal;
import com.yunyin.print.service.constant.Constant;
import com.yunyin.print.service.dao.TerminalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
@RestController
@RequestMapping("/terminal/*")
public class TerminalController {
    @Autowired
    private TerminalDao terminalDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<PTerminal>> getTerminalListBySiteId(Long siteId) {
        if (siteId == null) {
            throw new ServiceException(Constant.EXCEPTION_CODE_NULL, "siteId cannot be null or enpty");
        }
        List<PTerminal> terminals = terminalDao.getTerminalsBySiteId(siteId);

        return ResultHelper.genResultWithSuccess(terminals);
    }
}


