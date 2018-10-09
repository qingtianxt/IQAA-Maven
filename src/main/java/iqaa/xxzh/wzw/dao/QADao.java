package iqaa.xxzh.wzw.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import iqaa.xxzh.msl.bean.Question;

public interface QADao {

	List<Question> getQuestionByCriteria(DetachedCriteria criteria);

	List<Question> getStandardQuestion(String question);
	
}
