package iqaa.xxzh.msl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import iqaa.xxzh.msl.adapter.NlpirAdapter;
import iqaa.xxzh.msl.adapter.TokenizerAdapter;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.Question;

/**
 * @author Administrator
 *	答案问题提取器
 */
public abstract class AnswerExtractor{
	
	private File file;
	
	public AnswerExtractor(File file){
		this.setFile(file);
	}
	
	public abstract List<Answer> getAnswers()throws IOException;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public static AnswerExtractor getHtmlExtractor( File f){
		return new HtmlAnswerExtractor(f);
	}
}



class HtmlAnswerExtractor extends AnswerExtractor{

	public HtmlAnswerExtractor(File file) {
		super(file);
	}

	@Override
	public List<Answer> getAnswers() throws IOException {
		File f = this.getFile();
		HtmlParser htmlParser = new HtmlParser();
		// 获取文件中的文本
		String text = htmlParser.getText(new FileInputStream(f));
		// 将文本安每行分开
		String[] split = text.split("\n");
		List<Answer> list = new LinkedList<Answer>();
		TokenizerAdapter tokenizer = new NlpirAdapter();
		// 初始化调用NLPIR库
		for (String item : split) {
			if(item.isEmpty())
				continue;
			// 构造答案
			Answer answer = new Answer();
			answer.setContent(item);
			answer.setAddtime(new Date());
			
			// 调用C代码分析答案中的关键词
			Set<String> split2 = tokenizer.getKeyWords(item);
			// 将关键词作为答案的问题
			Set<Question> set = new HashSet<>();
			for (String q : split2) {
				if(q.isEmpty())
					continue;
				Question quest = new Question();
				// 把第一个关键词作为标准问题
				if(answer.getStandardQuestion() == null){
					answer.setStandardQuestion(quest);
					continue;
				}
				quest.setContent(q);
				set.add(quest);
			}
			answer.setExtendQuestion(set);
			list.add(answer);
		}
		return list;
	}
	
}

