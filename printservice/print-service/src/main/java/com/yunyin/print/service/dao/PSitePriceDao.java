package com.yunyin.print.service.dao;

import com.yunyin.print.common.model.PSitePrice;
import com.yunyin.print.service.mapper.PSitePriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
@Repository
public class PSitePriceDao {
    @Autowired
    private PSitePriceMapper masterPSitePriceMapper;
    @Autowired
    private PSitePriceMapper slavePSitePriceMapper;

    public List<PSitePrice> findBySiteId(Long siteId) {
        return slavePSitePriceMapper.findBySiteId(siteId);
    }
}
