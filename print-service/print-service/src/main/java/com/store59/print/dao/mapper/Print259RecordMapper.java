/**
 * 
 */
package com.store59.print.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.store59.print.common.model.gala259user.Print259Record;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:50:57
 * @since 1.0
 */
public interface Print259RecordMapper {
	
	Print259Record findDormFinish259Time(Integer dormId);
	
	int update259RecordValidValue(@Param("dormId") Integer dormId,@Param("status") Byte status);
}
