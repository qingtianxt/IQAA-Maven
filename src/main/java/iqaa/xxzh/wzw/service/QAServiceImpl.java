package iqaa.xxzh.wzw.service;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import iqaa.xxzh.msl.adapter.JcseAdapter;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.wzw.dao.QADao;

/**
 * @author Administrator
 * 王志伟
 */
public class QAServiceImpl implements QAService {
	private QADao qaDao;

	public void setQaDao(QADao qaDao) {
		this.qaDao = qaDao;
	}

	/**
	 * 根据问题智能获取答案
	 */
	@Override
	public Answer getByQuestion(String question) {
		// 匹配的顺序
		// 1.精确匹配
		// 2.模糊匹配
		// 3.问题句子的分解，分词找出关键词,进行模糊匹配
		System.out.println(question);

		List<Question> list = qaDao.getStandardQuestion(question);
		if (null != list && list.size() > 0) {
			System.out.println(1);
			for (Question question2 : list) {
				System.out.println(question2);
			}
			Answer a = list.get(0).getStandardAnswer();
			return a;
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(Question.class, "q");
		criteria.add(Restrictions.like("q.content", "%" + question + "%"));
		List<Question> list1 = qaDao.getQuestionByCriteria(criteria);
		if (null != list1 && list1.size() > 0) {
			System.out.println(2);
			for (Question question2 : list1) {
				System.out.println(question2);
			}
			Answer a = list1.get(0).getStandardAnswer();
			return a;
		}

		System.out.println(3);
		try {
			JcseAdapter jc = new JcseAdapter();
			Set<String> set = jc.getKeyWords(question);
			System.out.println(set);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
