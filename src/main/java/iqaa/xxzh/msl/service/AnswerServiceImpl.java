package iqaa.xxzh.msl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.dao.AnswerDao;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService{

	@Autowired private AnswerDao answerDao;
	@Override
	public Answer getAnswer(Long id) {
		Answer answer = answerDao.findById(id);
		return answer;
	}

}
