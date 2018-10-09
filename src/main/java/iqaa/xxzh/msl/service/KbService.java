package iqaa.xxzh.msl.service;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.vo.AnswerQueryInfo;
import iqaa.xxzh.msl.vo.PageBean;

public interface KbService {

	PageBean<Answer> getList(AnswerQueryInfo query);

	void add(Answer answer);

	void delete(Answer answer);

	boolean update(Answer answer);

}
