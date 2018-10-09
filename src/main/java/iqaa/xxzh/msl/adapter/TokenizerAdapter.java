package iqaa.xxzh.msl.adapter;

import java.util.Set;


public interface TokenizerAdapter {

	/**
	 * 获取文本中的关键词
	 * @param content
	 * @return	关键词集合
	 */
	Set<String> getKeyWords( String content);

	String getTokenize(String doc);
}
