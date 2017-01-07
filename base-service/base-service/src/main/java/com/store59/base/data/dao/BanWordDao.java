package com.store59.base.data.dao;

import com.store59.base.common.model.BanWord;
import com.store59.base.common.filter.BanWordFilter;
import com.store59.base.data.mapper.BanWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created on 2016-04-22.
 */
@Repository
public class BanWordDao {
    @Autowired
    private BanWordMapper masterBanWordMapper;
    @Autowired
    private BanWordMapper slaveBanWordMapper;

    public BanWord findBanWord(Integer bid){
        return slaveBanWordMapper.select(bid);
    }
    
    public List<BanWord> findBanWordList(BanWordFilter filter){
        return slaveBanWordMapper.findListByFilter(filter);
    }

    public BanWord addBanWord(BanWord record){
        return masterBanWordMapper.insert(record) == 0 ? null : record;
    }

    public Boolean updateBanWord(BanWord record){
        return masterBanWordMapper.update(record) == 0 ? false : true;
    }

    public Boolean deleteBanWord(Integer bid){
        return masterBanWordMapper.delete(bid) == 0 ? false : true;
    }
}

