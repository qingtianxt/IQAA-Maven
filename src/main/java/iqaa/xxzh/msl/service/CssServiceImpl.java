package iqaa.xxzh.msl.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.msl.bean.CssSelector;
import iqaa.xxzh.msl.dao.CssSelectorDao;
import iqaa.xxzh.msl.vo.CssQueryBean;
import iqaa.xxzh.msl.vo.PageBean;

@Service("cssService")
public class CssServiceImpl implements CssService {

	@Resource(name="cssSelectorDao")
	private CssSelectorDao selectorDao;

	@Override
	public CssSelector getByid(Long id) {
		CssSelector selector = selectorDao.findById(id);
		return selector;
	}

	@Transactional
	public String delete(CssSelector selector) {
		// 先根据ID查找
		CssSelector cssSelector = selectorDao.findById(selector.getId());
		if (cssSelector == null){
			return "指定id的规则不存在";
		}
		if(this.isUpdateable(cssSelector)){
			return "系统内置规则不能删除";
		}
		selectorDao.delete(cssSelector);
		return "删除成功";
	}

	@Transactional
	public String save(CssSelector selector) {
		if(isUpdateable(selector))
			return "系统内置规则不能修改";
		selectorDao.saveOrUpdate(selector);
		return "保存成功";
	}

	@Override
	public PageBean<CssSelector> getList(CssQueryBean queryBean) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CssSelector.class);
		String keyword = queryBean.getKeyword();
		if (keyword != null){
			criteria.add(Restrictions.like("name", keyword,MatchMode.ANYWHERE));
			criteria.add(Restrictions.like("questionSelector", keyword,MatchMode.ANYWHERE));
			criteria.add(Restrictions.like("answerSelector", keyword,MatchMode.ANYWHERE));
			criteria.add(Restrictions.like("themeSelector", keyword,MatchMode.ANYWHERE));
		}
		int currentPage = queryBean.getCurrentPage();
		int pageSize = queryBean.getPageSize();
		PageBean<CssSelector> list = selectorDao.findByCriteria(criteria, currentPage, pageSize);
		return list;
	}
	
	protected boolean isUpdateable(CssSelector cssSelecotr){
		String [] array = {"系统规则1","系统规则2","系统规则3","系统规则4","系统规则5","系统规则6","系统规则7","系统规则8","系统规则9"};
		for (String name : array) {
			if(StringUtils.equals(name, cssSelecotr.getName().trim()))
				return true;
		}
		return false;
	}
}
