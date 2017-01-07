package com.store59.print.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.PrintOrderFilter;
import com.store59.print.common.model.gala.GalaPrintRelation;
import com.store59.print.dao.mapper.GalaPrintRelationMapper;

@Repository
public class GalaPrintRelationDao {

    @Autowired
    private GalaPrintRelationMapper masterGalaPrintRelationMapper;

    @Autowired
    private GalaPrintRelationMapper slaveGalaPrintRelationMapper;
    
    public int insert(GalaPrintRelation printRelation) {
        return masterGalaPrintRelationMapper.insert(printRelation);
    }
    
    public int findGalaAmountByFilter(PrintOrderFilter filter){
    	return slaveGalaPrintRelationMapper.findGalaAmountByFilter(filter);
    }
}
