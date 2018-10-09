package iqaa.xxzh.msl.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import iqaa.xxzh.msl.bean.Question;

@Repository("questionDao")
public class QuestionDaoImpl extends BaseDao<Question> implements QuestionDao {

	@Override
	public Question findById(Long id) {
		HibernateTemplate htp = getHibernateTemplate();
		List<?> find = htp.find("from Question where id=?",id);
		Question q = find.isEmpty() ? null:(Question)find.get(0);
		return q;
	}

	@Override
	public Question findByContent(String quesContent) {
		if(StringUtils.isEmpty(quesContent)){
			return null;
		}
		List<?> list = getHibernateTemplate().find("from Question where content=?", quesContent);
		Question q = list.isEmpty()? null:(Question)list.get(0);
		return q;
	}


}
