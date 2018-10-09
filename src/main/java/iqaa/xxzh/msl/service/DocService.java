package iqaa.xxzh.msl.service;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.vo.PageBean;
import iqaa.xxzh.msl.vo.DocQueryInfo;

public interface DocService extends Serializable {

	boolean saveFile(DocBean doc);

	PageBean<DocBean> getList(DocQueryInfo queryInfo);

	void delete(DocBean doc);

	DocBean getById(Long id);

	void saveFile(List<DocBean> list);
	
	String getDocContent(DocBean doc);
	
	void analysis(InputStream id) throws Exception;
	
	/**
	 * 分析文件file，从中提取QA对
	 * @param file
	 */
	@Deprecated
	public List<Answer> extract(File file);

	/**
	 * 根据固定规则提取qa对
	 * 该方法已过时，请使用QuestionService.select()
	 * @param doc
	 * @return
	 */
	@Deprecated
	List<Answer> extractByRegulation(DocBean doc);

	List<Question> extract(DocBean doc);

	void updateFrequency(List<DocBean> docBeans);
}
