package iqaa.xxzh.msl.vo;

import iqaa.xxzh.msl.bean.CssSelector;

public class CssQueryBean extends PageBean<CssSelector> {
	
	public CssQueryBean(){
		setCurrentPage(1);
		setPageSize(10);
	}
	
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "CssQueryBean [keyword=" + keyword + "]";
	}
	
}
