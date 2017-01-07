/**
 * 
 */
package com.store59.print.dao.mapper;

import java.util.List;

import com.store59.print.common.filter.Print259UserFilter;
import com.store59.print.common.model.gala259user.Print259User;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午2:29:06
 * @since 1.0
 */
public interface Print259UserMapper {
 
	List<Print259User> findByFilter(Print259UserFilter filter);
	
	Integer findCountByFilter(Print259UserFilter filter);

}
