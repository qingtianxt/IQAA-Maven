package iqaa.xxzh.msl.dao;

import iqaa.xxzh.msl.bean.CssSelector;

public interface CssSelectorDao extends Dao<CssSelector> {

	CssSelector findById(Long id);

}
