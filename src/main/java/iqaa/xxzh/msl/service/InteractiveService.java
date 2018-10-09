package iqaa.xxzh.msl.service;

import java.util.List;

import iqaa.xxzh.msl.bean.Answer;

public interface InteractiveService {

	/**
	 * 问答交互，根据问题question，检索数据中答案，将最合适的前5条答案返回
	 * @param question	问题内容
	 * @return			答案集合
	 */
	List<Answer> interactive(String question);

}
