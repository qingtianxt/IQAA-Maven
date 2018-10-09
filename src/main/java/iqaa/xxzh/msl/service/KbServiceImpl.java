package iqaa.xxzh.msl.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.AnswerDao;
import iqaa.xxzh.msl.dao.QuestionDao;
import iqaa.xxzh.msl.vo.AnswerQueryInfo;
import iqaa.xxzh.msl.vo.PageBean;

@Service("kbService") @Transactional
public class KbServiceImpl implements KbService {

	@Override
	public PageBean<Answer> getList(AnswerQueryInfo query) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Answer.class,"a");
		String answer = query.getAnswer();
		String question = query.getQuestion();
		Disjunction dis = Restrictions.disjunction();
		if(answer != null && !answer.isEmpty()){
			SimpleExpression like = Restrictions.like("a.content", "%"+answer+"%");
			dis.add(like);
		}
		if(question != null && !question.isEmpty()){
			criteria.createAlias("standardQuestion", "q");
			SimpleExpression like = Restrictions.like("q.content", "%"+question+"%",MatchMode.ANYWHERE);
			dis.add(like);
			
//			dis.add(Restrictions.like("standardQuestion.content", "%"+question+"%"));
//			DetachedCriteria questionCriteria = DetachedCriteria.forClass(Question.class);
//			questionCriteria.add(Restrictions.like("content", question));
//			questionCriteria.setProjection(Property.forName("id"));
		}
		criteria.add(dis);
		Date start = query.getStart();
		if(start!=null){
			criteria.add(Restrictions.ge("a.addtime", start));
		}
		Date end = query.getEnd();
		if(end!=null){
			criteria.add(Restrictions.le("a.addtime", end));
		} 
		PageBean<Answer> pageBean = answerDao.findByCriteria(criteria, query.getCurrentPage(), query.getPageSize());
		return pageBean;
	}

	@Resource(name="answerDao") AnswerDao answerDao;
	@Resource(name="questionDao") QuestionDao questionDao;
	@Override
	public void add(Answer answer) {
		Question question = answer.getStandardQuestion();
		question.setOperator(answer.getOperator());
		question.setClickRate(0l);
		question.setSubject(answer.getSubject());
		Date addtime = new Date();
		question.setAddtime(addtime);
		answer.setAddtime(addtime);
		question.setStandardAnswer(answer);
		
		questionDao.saveOrUpdate(question);
		answerDao.saveOrUpdate(answer);
	}
	@Override
	public void delete(Answer answer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Answer.class);
		criteria.add(Restrictions.eq("id", answer.getId()));
		List<Answer> a = answerDao.findByCriteria(criteria, Answer.class);
		if(a.isEmpty())
			throw new RuntimeException("指定id的问题不存在！");
		Answer an = a.get(0);
		Question question = an.getStandardQuestion();
		questionDao.delete(question);
		answerDao.delete(an);
	}
	@Override
	public boolean update(Answer answer) {
		Answer findById = answerDao.findById(answer.getId());
		BeanUtils.copyProperties(answer, findById);
		answerDao.saveOrUpdate(findById);
		return true;
	}
}
