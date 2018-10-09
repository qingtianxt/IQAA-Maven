package iqaa.xxzh.msl.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import iqaa.xxzh.msl.vo.PageBean;

public class BaseDao<T> extends HibernateDaoSupport implements Dao<T> {

	@Override
	@SuppressWarnings("unchecked")
	public PageBean<T> findByCriteria(DetachedCriteria criteria, int start, int max) {
		HibernateTemplate ht = getHibernateTemplate();
		PageBean<T> pageBean = new PageBean<T>();
		criteria.setProjection(Projections.rowCount());
		List<?> o = ht.findByCriteria(criteria);
		int totalCount = Integer.parseInt(o.get(0).toString());
		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<T> list = (List<T>) ht.findByCriteria(criteria, start*max-max, max);
		pageBean.setTotalCount(totalCount);
		pageBean.setList(list);
		pageBean.setCurrentPage(start);
		pageBean.setPageSize(max);
		pageBean.setTotalPage(totalCount%max==0? totalCount/max:totalCount+1);
		return pageBean;
	}

	

	@Override
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}



	@Override
	public <G> List<G> findByCriteria(DetachedCriteria criteria,Class<G> clazz) {
		@SuppressWarnings("unchecked")
		List<G> list = (List<G>) getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	
	@Autowired 
	@Qualifier("sessionFactory")
	public void setMySessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}



	@Override
	public void saveOrUpdate(Collection<T> list) {
		HibernateTemplate ht = getHibernateTemplate();
		int i = 0;
		for (T t : list) {
			ht.saveOrUpdate(t);
			i++;
			if(i % 50 == 0){
				ht.flush();
				ht.clear();
			}
		}
		ht.flush();
		ht.clear();
	}



}
