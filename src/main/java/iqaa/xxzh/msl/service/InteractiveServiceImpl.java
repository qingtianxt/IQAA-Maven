package iqaa.xxzh.msl.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.dao.AnswerDao;

@Service("interactiveService")
public class InteractiveServiceImpl implements InteractiveService {

	@Override
	public List<Answer> interactive(String question) {
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Answer.class,"a");
			detachedCriteria.createAlias("standardQuestion", "q");
			detachedCriteria.add(Restrictions.like("q.content", "%"+question+"%"));
			List<Answer> list = answerDao.findByCriteria(detachedCriteria, Answer.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	@Autowired
//	private QuestionDao dao;

	@Autowired
	private AnswerDao answerDao;
}
