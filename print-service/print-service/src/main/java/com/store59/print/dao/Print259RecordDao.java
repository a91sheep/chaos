/**
 * 
 */
package com.store59.print.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.model.gala259user.Print259Record;
import com.store59.print.dao.mapper.Print259RecordMapper;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:49:19
 * @since 1.0
 */
@Repository
public class Print259RecordDao {
	@Autowired
	private Print259RecordMapper masterPrint259RecordMapper;

	@Autowired
	private Print259RecordMapper slavePrint259RecordMapper;


	public Print259Record findDormFinish259Time(Integer dormId) {
		return slavePrint259RecordMapper.findDormFinish259Time(dormId);
	}
	
	public int update259RecordValidValue(Integer dormId,Byte status){
		return masterPrint259RecordMapper.update259RecordValidValue(dormId,status);
	}
}
