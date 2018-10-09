package iqaa.xxzh.msl.service;

import iqaa.xxzh.msl.bean.Answer;

public interface AnswerService {

	/**
	 * 根据问题的ID获取问题和答案信息
	 * @param id
	 * @return
	 */
	Answer getAnswer(Long id);
 
}
