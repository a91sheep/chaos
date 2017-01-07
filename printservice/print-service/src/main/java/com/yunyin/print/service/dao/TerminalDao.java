package com.yunyin.print.service.dao;

import com.yunyin.print.common.model.PTerminal;
import com.yunyin.print.service.mapper.TerminalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
@Repository
public class TerminalDao {
    @Autowired
    private TerminalMapper masterTerminalMapper;
    @Autowired
    private TerminalMapper slaveTerminalMapper;

    public List<PTerminal> getTerminalsBySiteId(Long siteId) {
        return slaveTerminalMapper.findBySiteId(siteId);
    }
}
