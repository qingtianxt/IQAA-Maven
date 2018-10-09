package iqaa.xxzh.msl.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import iqaa.xxzh.msl.bean.CssSelector;

@Repository("cssSelectorDao")
public class CssSelectorDaoImpl extends BaseDao<CssSelector> implements CssSelectorDao{


	@Override
	public CssSelector findById(Long id) {
		List<?> list = getHibernateTemplate().find("from CssSelector where id=?", id);
		if (list.isEmpty())
			return null;
		else
			return (CssSelector) list.get(0);
	}

}
