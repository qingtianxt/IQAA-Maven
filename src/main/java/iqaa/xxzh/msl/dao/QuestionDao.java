package iqaa.xxzh.msl.dao;

import iqaa.xxzh.msl.bean.Question;

public interface QuestionDao extends Dao<Question> {

	/**
	 * 通过问题的id好查找出问题的具体信息
	 * @param id
	 */
	Question findById(Long id);

	Question findByContent(String quesContent);

}
