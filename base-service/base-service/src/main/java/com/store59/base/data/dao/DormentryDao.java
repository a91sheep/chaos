package com.store59.base.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.base.common.filter.DormentryFloorFilter;
import com.store59.base.common.model.Dormentry;
import com.store59.base.data.mapper.DormentryMapper;

@Repository
public class DormentryDao {
    @Autowired
    private DormentryMapper masterDormentryMapper;
    @Autowired
    private DormentryMapper slaveDormentryMapper;

    public List<Dormentry> getDormentryListByDormId(int dormId) {
        return slaveDormentryMapper.selectByDormID(dormId);
    }

    public List<Dormentry> getDormentryListBySiteId(int dormId) {
        return slaveDormentryMapper.selectBySiteId(dormId);
    }

    public Boolean updateDormentry(Dormentry dormentry) {
        int rows = masterDormentryMapper.updateByPrimaryKeySelective(dormentry);
        if (rows > 0) {
            return true;
        }
        return false;
    }

    public Dormentry getDormentry(Integer dormentryId) {
        return slaveDormentryMapper.selectByPrimaryKey(dormentryId);
    }

    public List<Dormentry> getFloorListByFilter(DormentryFloorFilter filter) {
        return slaveDormentryMapper.getFloorListByFilter(filter);
    }

}
