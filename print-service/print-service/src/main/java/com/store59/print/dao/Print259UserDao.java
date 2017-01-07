/**
 * 
 */
package com.store59.print.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store59.print.common.filter.Print259UserFilter;
import com.store59.print.common.model.gala259user.Print259User;
import com.store59.print.dao.mapper.Print259UserMapper;

/**
 * @author <a href="mailto:shiz@59store.com">柯南</a>
 * @version 1.0 2016年6月20日 下午2:56:31
 * @since 1.0
 */
@Repository
public class Print259UserDao {
	@Autowired
	private Print259UserMapper masterPrint259UserMapper;

	@Autowired
	private Print259UserMapper slavePrint259UserMapper;


	public List<Print259User> find259UserByFilter(Print259UserFilter filter) {
		return slavePrint259UserMapper.findByFilter(filter);
	}
	
	public Integer findCountByFilter(Print259UserFilter filter){
		return slavePrint259UserMapper.findCountByFilter(filter);
	}
}
