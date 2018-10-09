package iqaa.xxzh.msl.service;

import iqaa.xxzh.msl.bean.CssSelector;
import iqaa.xxzh.msl.vo.CssQueryBean;
import iqaa.xxzh.msl.vo.PageBean;

public interface CssService {

	CssSelector getByid(Long id);

	String delete(CssSelector selector);

	String save(CssSelector selector);

	PageBean<CssSelector> getList(CssQueryBean queryBean);

}
