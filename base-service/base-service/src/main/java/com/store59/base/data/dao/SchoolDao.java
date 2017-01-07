/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.base.data.dao;

import com.store59.base.common.model.School;
import com.store59.base.data.mapper.SchoolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author <a href="mailto:baixf@59store.com">白白</a>
 * @version 1.0 16/5/9
 * @since 1.0
 */
@Repository
public class SchoolDao {
    @Autowired
    private SchoolMapper masterSchoolMapper;

    @Autowired
    private SchoolMapper slaveSchoolMapper;

    public int insert(School school) {
        return masterSchoolMapper.insert(school);
    }

    public int update(School school) {
        return masterSchoolMapper.update(school);
    }

    public int delete(Integer id) {
        return masterSchoolMapper.delete(id);
    }

    public School getSchool(Integer id) {
        return slaveSchoolMapper.getSchool(id);
    }

    public List<School> findByNmae(String name) {
        return slaveSchoolMapper.findByName(name);
    }

    public Integer getCountByName(String name) {
        return slaveSchoolMapper.getCountByName(name);
    }
}
