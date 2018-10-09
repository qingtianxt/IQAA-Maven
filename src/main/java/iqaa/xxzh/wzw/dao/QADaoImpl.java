package iqaa.xxzh.wzw.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import iqaa.xxzh.msl.bean.Question;

public class QADaoImpl extends HibernateDaoSupport implements QADao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getQuestionByCriteria(DetachedCriteria criteria) {
		
		return (List<Question>) this.getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getStandardQuestion(String question) {
		return (List<Question>) this.getHibernateTemplate().find("from Question where content=?", question);
	}


}
