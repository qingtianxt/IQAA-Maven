package iqaa.xxzh.msl.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.vo.PageBean;

@Repository("answerDao")
public class AnswerDaoImpl extends BaseDao<Answer> implements AnswerDao {

	@Override
	public Answer findById(Long id) {
		HibernateTemplate ht = getHibernateTemplate();
		List<?> answers = ht.find("from Answer where id=?", id);
		if(answers.isEmpty())
			return null;
		Answer answer = (Answer) answers.get(0);
		return answer;
	}
	
	@Override
	public PageBean<Answer> findByCriteria(DetachedCriteria criteria, int start, int max) {
		PageBean<Answer> findByCriteria = super.findByCriteria(criteria, start, max);
		return findByCriteria;
	}

}
