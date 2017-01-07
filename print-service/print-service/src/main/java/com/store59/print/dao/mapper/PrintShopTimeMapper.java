/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.store59.print.common.model.gala259user.PrintShopTime;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月21日 下午4:50:57
 * @since 1.0
 */
public interface PrintShopTimeMapper {

	List<PrintShopTime> findPrintShopTimeByDormId(@Param("dormId") Integer dormId, @Param("time") Long time);

	Integer findCountPrintShopTimeByDormId(@Param("dormId") Integer dormId, @Param("time") Long time);
}
