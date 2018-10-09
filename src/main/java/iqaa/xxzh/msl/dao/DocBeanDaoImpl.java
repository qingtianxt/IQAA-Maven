package iqaa.xxzh.msl.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;

import iqaa.xxzh.msl.bean.DocBean;

@Component(value="docDao")
public class DocBeanDaoImpl extends BaseDao<DocBean> implements DocBeanDao {

	@Override
	public DocBean findById(Long id) {
		DocBean docBean = getHibernateTemplate().get(DocBean.class, id);
		return docBean;
	}

	@Override
	public Integer totalCount() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocBean.class);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		String s = list.get(0).toString();
		return Integer.valueOf(s);
	}

	@Override
	public DocBean findByName(String docName) {
		List<?> find = getHibernateTemplate().find("from DocBean where docName=?", docName);
		DocBean doc = find.isEmpty()? null:(DocBean)find.get(0);
		return doc;
	}


}
