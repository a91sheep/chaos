package com.store59.base.data.mapper;

import com.store59.base.common.model.AreaJoin;

import java.util.List;

/**
 * Created by solder on 15/9/10.
 */
public interface AreaJoinMapper {

    AreaJoin getBySiteId(int siteId);

    List<AreaJoin> findBySiteIds(List<Integer> siteIds);
}