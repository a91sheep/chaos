package com.store59.base.data.mapper;

import com.store59.base.common.model.BanWord;
import com.store59.base.common.filter.BanWordFilter;

import java.util.List;

/**
 * BanWord mybatis mapper
 * 
 * Created on 2016-04-22.
 */
public interface BanWordMapper {

    BanWord select(Integer bid);

    int insert(BanWord record);

    int update(BanWord record);

    int delete(Integer bid);

    List<BanWord> findListByFilter(BanWordFilter filter);
}
