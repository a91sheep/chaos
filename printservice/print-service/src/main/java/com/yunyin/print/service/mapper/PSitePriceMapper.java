package com.yunyin.print.service.mapper;

import com.yunyin.print.common.model.PSitePrice;

import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/15
 * @since 1.0
 */
public interface PSitePriceMapper {
    List<PSitePrice> findBySiteId(Long siteId);
}
