/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.common.api;

import com.store59.base.common.model.School;
import com.store59.kylin.common.model.Result;

import java.util.List;

/**
 * 学校接口
 *
 * @author <a href="mailto:baixf@59store.com">白白</a>
 * @version 1.0 16/5/9
 * @since 1.0
 */
public interface SchoolApi {
    /**
     * 获取学校信息
     *
     * @param id
     * @return
     */
    Result<School> getSchool(Integer id);

    /**
     * 根据学校名,获取学校信息
     *
     * @param name
     * @return
     */
    Result<List<School>> findByName(String name);

    /**
     * 新增学校
     * @param school
     * @return
     */
    Result insert(School school);

    /**
     * 更新学校
     * @param school
     * @return
     */
    Result update(School school);

    /**
     * 删除学校
     * @param id
     * @return
     */
    Result delete(Integer id);
}
