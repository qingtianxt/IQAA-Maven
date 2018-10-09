package iqaa.xxzh.msl.dao;

import iqaa.xxzh.msl.bean.Answer;

public interface AnswerDao extends Dao<Answer> {

	/**
	 * 根据问题的ID查找问题
	 * @param id
	 * @return
	 */
	Answer findById(Long id);

}
