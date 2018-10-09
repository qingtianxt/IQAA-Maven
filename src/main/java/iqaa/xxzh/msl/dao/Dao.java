package iqaa.xxzh.msl.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import iqaa.xxzh.msl.vo.PageBean;

public interface Dao<T> {

	/**
	 * @param criteria	查询条件
	 * @param start		当前页码
	 * @param max		每页显示数量
	 * @return			
	 */
	PageBean<T> findByCriteria(DetachedCriteria criteria,int start,int max);
	
	<G> List<G> findByCriteria(DetachedCriteria criteria,Class<G> clazz);
	
	void delete(T entity);
	
	void saveOrUpdate(T entity);
	
	void saveOrUpdate(Collection<T> list);
	
}
