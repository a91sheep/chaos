/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.data.mapper;

import com.store59.base.common.model.School;

import java.util.List;

/**
 *
 * @author <a href="mailto:baixf@59store.com">白白</a>
 * @version 1.0 16/5/9
 * @since 1.0
 */
public interface SchoolMapper {
    School getSchool(int id);

    List<School> findByName(String name);

    int delete(int id);

    int update(School school);

    int insert(School school);

    Integer getCountByName(String name);
}
