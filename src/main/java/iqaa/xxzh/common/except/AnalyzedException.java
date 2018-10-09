package iqaa.xxzh.common.except;

import iqaa.xxzh.msl.bean.DocBean;

public class AnalyzedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AnalyzedException(String name){
		super("文档\""+name+"\"已经分析过了...");
	}
	public AnalyzedException(DocBean docBean){
		this(docBean.getDocName());
	}
}
