package iqaa.xxzh.msl.service;

import java.util.List;

import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;

public interface QuestionService {

	/**
	 * 根据HTML文档中的标签分析出提取出问题和答案的组合
	 * @param docBean
	 * @return
	 */
	List<Question> select(DocBean docBean);

	/**
	 * 从value中提取出QA信息，过滤掉已经添加过得信息
	 * @param value		由List<Question>对象转化的json数据
	 * @return			List<Question>对象
	 */
	List<Question> backselect(String value);

	/**
	 * @param question
	 * @return
	 */
	@Deprecated
	String save(Question question);
  
	
	/**
	 * 使用神经网络分析文档
	 * @param docBean
	 * @return
	 */
	List<Question> selectbynn(DocBean docBean);

}
