package iqaa.xxzh.msl.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.CssSelector;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Lexicon;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.AnswerDao;
import iqaa.xxzh.msl.dao.CssSelectorDao;
import iqaa.xxzh.msl.dao.DocBeanDao;
import iqaa.xxzh.msl.dao.LexiconDao;
import iqaa.xxzh.msl.dao.QuestionDao;
import iqaa.xxzh.msl.nn.Matrix;
import iqaa.xxzh.msl.nn.Network;
import iqaa.xxzh.msl.utils.Date;

@Transactional
public class QuestionServiceImpl implements QuestionService { 
	@Autowired 
	private QuestionDao dao;
	@Autowired 
	private AnswerDao answerDao ;
	@Autowired 
	private DocBeanDao docDao;
	
	@Resource(name="network") 
	private Network nn;
	@Resource(name="cssSelectorDao")
	private CssSelectorDao cssSelectorDao;
	
	public List<Question> select(DocBean docBean) {
		// 根据文档的ID查找出文档的内容
		DetachedCriteria criteria = DetachedCriteria.forClass(DocBean.class);
		criteria.add(Restrictions.idEq(docBean.getId()));
		List<DocBean> docLst = dao.findByCriteria(criteria, DocBean.class);
		// 没有找到指定ID的文档
		if(docLst.isEmpty())
			return null;
		else{
			// 获取文档
			DocBean doc = docLst.get(0);
			// 文档状态 0表示还未分析
			int mark = doc.getMark();
			if(!(mark==0||mark==2||mark==4||mark==6))
				throw new AnalyzedException(doc.getDocName());
			// 文档信息
			String content = doc.getContent();
			// 解析HTML文档
			Document document = Jsoup.parse(content);
			//List<Question> list = fromDocument(document,doc,docBean.getUserBean());
			List<Question> list = analysis(document, doc, docBean.getUserBean());
			// 保存分析结果
			dao.saveOrUpdate(list);
			// 修改文档分析状态
			doc.setMark(mark|1);
			docDao.saveOrUpdate(doc);
			return list;
		}
	}
	private List<Question> analysis(Document document,DocBean doc,UserBean userBean){
		List<Question> list = new ArrayList<>();
		DetachedCriteria criteria = DetachedCriteria.forClass(iqaa.xxzh.msl.bean.CssSelector.class);
		List<CssSelector> selectors = cssSelectorDao.findByCriteria(criteria, CssSelector.class);
		for (CssSelector selector : selectors) {
			Elements questionElements = document.select(selector.getQuestionSelector());
			Elements answerElements = document.select(selector.getAnswerSelector());
			Elements themeElements = document.select(selector.getThemeSelector());
			int questionSize = questionElements.size();
			int answerSize = answerElements.size();
			int themeSize = themeElements.size();
			String theme = null;
			
			if(themeSize > 0){
				theme = themeElements.get(themeSize-1).text();
			}
			for(int i = 0; i < questionSize&& i<answerSize; i++){
				Question question = new Question();
				question.setOperator(userBean);
				String questionContext = questionElements.get(i).text();
				question.setContent(questionContext);
				
				String answerText = answerElements.get(i).text();
				Answer answer = new Answer();
				answer.setContent(answerText);
				question.setStandardAnswer(answer);
				
				Date date = new Date();
				answer.setAddtime(date);
				question.setAddtime(date);
				
				answer.setOperator(userBean);
				answer.setSubject(theme);
				answer.setDoc(doc);
				question.setSubject(theme);
				question.setDoc(doc);
				question.setClickRate(0l);
				list.add(question);
			}
		}
		return list;
	}
	@Deprecated
	protected List<Question> fromDocument(Document document,DocBean doc, UserBean operator) {
		// 存放问题的数组
		List<Question> list = new ArrayList<>();
		// 存放主题的数组
		List<String> themes = new ArrayList<>();
		// 遍历主题选择器数组，查找主题
		for (String string : themeselectorLst) {
			Elements themeElements = document.select(string);
			for (Element element : themeElements) {
				String theme = element.text();
				themes.add(theme);
			}
		}
		// 问题选择器的个数
		int size = questionselectorLst.size();
		// 遍历所有问题和答案选择器
		for(int i = 0; i < size; i++){
			String questionselector = questionselectorLst.get(i);
			String answerselector = answerselectorLst.get(i);
			// 查询出HTML元素
			Elements questionElement = document.select(questionselector);
			Elements answerElement = document.select(answerselector);
			int questionSize = questionElement.size();
			int answerSize = answerElement.size();
			if(questionSize>0&&answerSize>0&&answerSize==questionSize){
				for(int j = 0; j < questionSize; j++){
					String answer = answerElement.get(j).text();
					String question = questionElement.get(j).text();
					
					// 主题个数
					int themeSize = themes.size();
					// 去最后一个主题作为答案和问题的主题
					String theme = themeSize <= 0 ? "":themes.get(themeSize-1);
					Date adddate = new Date();
					Answer a = new Answer();
					a.setContent(answer);
					a.setSubject(theme);
					a.setOperator(operator);
					a.setAddtime(adddate);
					a.setDoc(doc);
					Question q = new Question();
					q.setContent(question);
					q.setSubject(theme);
					q.setOperator(operator); 
					q.setStandardAnswer(a);
					q.setAddtime(adddate);
					q.setDoc(doc);
					list.add(q);
				}
			}
			
		}
		return list;
	}
	/**
	 * 查看指定的问题是否已在在数据库中存在了
	 * @param str	问题
	 * @return		如果问题已经存在返回true，否则返回false
	 */
	/*private boolean testExesis(String str) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Question.class);
		detachedCriteria.add(Restrictions.eq("content", str));
		List<Question> isEmpty = dao.findByCriteria(detachedCriteria, Question.class);
		return isEmpty.isEmpty();
	}*/
	
	

	/**
	 * 存放HTML文档选择器的数组
	 */
	private static List<String> answerselectorLst;
	private static List<String> questionselectorLst;
	private static List<String> themeselectorLst;
	static{
		answerselectorLst = new ArrayList<>();
		questionselectorLst = new ArrayList<>();
		themeselectorLst = new ArrayList<>();
		String fileName = "answerselector";
		String fileName2 = "questionselector";
		String fileName3 = "themeselector";
		readSelectors(fileName,answerselectorLst);
		readSelectors(fileName2,questionselectorLst);
		readSelectors(fileName3,themeselectorLst);
	}
	/**
	 * 从文档中读取HTML的CSS选择器
	 * @param fileName
	 * @param answerselectorLst
	 * @throws IOException
	 */
	private static void readSelectors(String fileName,List<String> answerselectorLst)  {
		BufferedReader reader = null;
		try {
			InputStream inputStream = QuestionServiceImpl.class.getResourceAsStream(fileName);
			InputStreamReader streamReader = new InputStreamReader(inputStream,"utf-8");
			reader = new BufferedReader(streamReader);
			while(reader.ready()){
				String selector = reader.readLine();
				answerselectorLst.add(selector);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(reader !=  null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
		}
	}
	
	

	@Override
	public List<Question> backselect(String value) {
		List<Question> list = new ArrayList<>();
		if(value != null){
			String[] split = value.split(",");
			List<Long> ids = new ArrayList<>();
			for (String string : split) {
				ids.add(Long.valueOf(string));
			}
			DetachedCriteria criteria = DetachedCriteria.forClass(Question.class);
			criteria.add(Restrictions.in("id", ids));
			list = dao.findByCriteria(criteria, Question.class);
		}
		return list;
	}
	@Override
	public String save(Question question) {
		Question question2 = dao.findById(question.getId());
		if(question2==null){
			return "问题不存在";
		}
		Date addtime = new Date();
		question2.setAddtime(addtime);
		Answer answer = question2.getStandardAnswer();
		answer.setAddtime(addtime);
		dao.saveOrUpdate(question2);
		answerDao.saveOrUpdate(answer);
		
		return "保存成功";
	}

	@Override
	public List<Question> selectbynn(DocBean docBean) {
		logger.info("selectbynn");
		DocBean bean = docDao.findById(docBean.getId());
		if(bean == null)
			throw new RuntimeException("没有找到指定id的文档信息");
		int mark = bean.getMark();
		if(!(mark==0||mark==1||mark==2||mark==3)){
			throw new AnalyzedException(bean);
		}
		String content = bean.getContent();
		// 构造HTML标签的正则表达式
		String regex_style = "<style [^>]*?>[\\s\\S]*?<\\/style>";
		String regex_script = "<script[^>]*?[\\s\\S]*?<\\/script>";
		String regex_html = "<[^>]+>";
		// 去除所有HTML标签
		String[] split = 
				content.split("("+regex_style+")|("+regex_script+")|("+regex_html+")");
		StringBuffer pertext = new StringBuffer();
		List<String> tmpstrs=new LinkedList<>();
		for (String clip : split) {
			if(!StringUtils.isEmptyOrWhitespaceOnly(clip)){
				pertext.append(clip);
				tmpstrs.add(clip);
			}
		}
		// 问题信息数组，这个数组中的对象只做中转存储，里面对象如果经过分析不符合条件可能不会添加
		List<Question> questions = new ArrayList<>();
		String text = pertext.toString();
		// 将文本向量化表示
		Matrix<Double> matrix = construct(tmpstrs, text,questions);
		// 深度学习预测结果0表示不适合，1表示适合
		System.out.println("模型预测开始.....");
		long t1 = System.currentTimeMillis();
		List<Integer> indexs = nn.predict(matrix);
		System.out.println("模型预测结束...用时"+(System.currentTimeMillis()-t1)+"ms");
		// 存放合适的问题信息的数组
		List<Question> tmps = new ArrayList<>();
		UserBean operator = docBean.getUserBean();
		for (int i = 0 ; i < indexs.size()&&i<questions.size(); i++) {
			int index = indexs.get(i);
			if(index==1){
				Question question = questions.get(i);
				String quesContent = question.getContent();
				/*Question t = dao.findByContent(quesContent);
				if(t!=null)
					continue;*/
				Date date = new Date();
				question.setAddtime(date);
				question.setClickRate(0l);
				question.setDoc(bean);
				question.setOperator(operator);
				
				int indexOf = text.indexOf(quesContent);
				if(indexOf!=-1){
					int start = indexOf > 100 ? indexOf - 100 : 0;
					int end = indexOf < pertext.length() - 100 ? indexOf + 100:pertext.length();
					CharSequence answerText = text.subSequence(start, end);
					Answer answer = question.getStandardAnswer();
					answer.setAddtime(date);
					answer.setDoc(bean);
					answer.setContent(answerText.toString());
					answer.setOperator(operator);
					tmps.add(question);
				}
			}
		}
		
		mark = mark|4;
		bean.setMark(mark);
		docDao.saveOrUpdate(bean);
		return tmps;
	}
	
	/**
	 * 构建一个矩阵向量
	 * @param strarray
	 * @param docContent
	 * @param questions
	 * @return
	 */
	private Matrix<Double> construct(List<String> strarray,String docContent, List<Question> questions){
		// 总的语句长度
		int length = strarray.size();
		// 线程个数
		int threadCount = length%500==0? length / 500:length/500+1;
		// 线程计数器
		CountDownLatch latch = new CountDownLatch(threadCount);
		// 存放矩阵数据的数组
		List<List<Double>> m = new ArrayList<>();
		// 存放线程的数组
		List<ConstructMatrixThread> threadArrays = new ArrayList<>();
		// 分割语句数组，构成子数组开启线程
		int currPos = 0;
		int endPos = 500;
		List<Double> tfidf = compress(tfidf(docContent), 100);
		while(currPos < length){
			if(endPos >= length)
				endPos = length-1;
			// 截取数组中的一段
			List<String> subarray = /*ArrayUtils.subarray(strarray, currPos, endPos);*/strarray.subList(currPos, endPos);
			// 子线程对象
			ConstructMatrixThread constructMatrixThread = new ConstructMatrixThread(subarray,questions,tfidf,latch);
			Thread thread = new Thread(constructMatrixThread);
			// 开启线程
			thread.start();
			currPos += 500;
			endPos += 500;
			threadArrays.add(constructMatrixThread);
		}
		try {
			// 等待所有线程结束
			System.out.println("等待所有子线程结束....");
			latch.await();
			System.out.println("所有子线程都已结束...");
			// 获取线程中的数据
			for (ConstructMatrixThread thread : threadArrays) {
				List<List<Double>> vector = thread.getVector();
				m.addAll(vector);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 构建矩阵对象
		Matrix<Double> matrix = new Matrix<Double>();
		matrix.setVector(m);
		return matrix;
	}
	
	class ConstructMatrixThread implements Runnable{
		private List<String> strarray;
		private List<Question> questions;
		private CountDownLatch latch;
		private List<List<Double>> mVector;
		private List<Double> tfidf;
		public ConstructMatrixThread(List<String> strarray, List<Question> questions,List<Double> tfidf,CountDownLatch latch) {
			this.strarray = strarray;
			this.questions = questions;
			this.latch = latch;
			this.tfidf = tfidf;
		}
		@Override
		public void run() {
			try {
				mVector = new ArrayList<>();
				for (String str : strarray) {
					if(str.trim().length()<4)
						continue;
					List<Double> v1 = str2matirx(str);
					v1.addAll(tfidf);
					mVector.add(v1);
					Question question = new Question();
					Answer answer = new Answer();
					question.setStandardAnswer(answer);
					question.setContent(str);
					questions.add(question);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				this.latch.countDown();
			}
			
		}
		public List<List<Double>> getVector(){
			return this.mVector;
		}
		
	}
	protected List<List<Double>> compressMatrix(List<List<Double>> m,int step){
		long start = System.currentTimeMillis();
		System.out.println("压缩矩阵...");
		List<List<Double>> matrix = new ArrayList<>();
		for (List<Double> list : m) {
			List<Double> row = new LinkedList<>();
			int currPos = 0;
			int stopPos = step;
			int size = list.size();
			while(stopPos <= size){
				Double tmp = 0d;
				for(int i = currPos; i < stopPos; i++){
					tmp += list.get(i);
				}
				row.add(tmp);
				currPos += step;
				stopPos += step;
			}
			matrix.add(row);
		}
		System.out.println("压缩矩阵完成....用时"+(System.currentTimeMillis()-start)+"ms");
		return matrix;
	}
	
	protected List<Double> compress(List<Double> docVector,int step) {
		int size = docVector.size();
		List<Double> list = new ArrayList<>(size/step+step);
		int currPos = 0;
		int stopPos = step;
		while(stopPos <= size){
			Double tmp = 0d;
			for(int i = currPos; i < stopPos; i++){
				tmp += docVector.get(i);
			}
			list.add(tmp);
			currPos += step;
			stopPos += step;
		}
		return list;
	}
	private List<Double> str2matirx(String sentance){
		if(words == null)
			words = lexiconDao.getLexicons();
		List<Double> vector = new LinkedList<>();
		int i = 0;
		int count = 0;
		for (String word : words) {
			count += countTimes(sentance, word);
//			vector.add(valueOf);
			i ++;
			if(i==100){
				vector.add(Double.valueOf(count));
				count = 0;
				i=0;
			}
		}
		return vector;
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 */
	private List<Double> tfidf(String doc){
		if(words == null)
			words = lexiconDao.getLexicons();
		List<Double> vector = new LinkedList<>();
		Integer totalCount = docDao.totalCount();
		List<Lexicon> list = lexiconDao.getWords();
		// 所有词语的个数
		int wordcount = 0;
		for (Lexicon lexicon : list) {
			int count = countTimes(doc,lexicon.getWord());
			wordcount += count;
			double x = count * Math.log(((double)totalCount+1)/(lexicon.getTimes()+1));
			vector.add(x);
		}
		
		List<Double> v = new LinkedList<>();
		for (Double integer : vector) {
			double rate = integer.doubleValue() / wordcount;
			v.add(rate);
		}
		return v;
	}

	/**
	 * 计算词组Word在doc中出现的次数
	 * @param doc
	 * @param word
	 * @return
	 */
	private int countTimes(String doc, String word) {
		int startIndex = 0;
		int curIndex = 0;
		int count = 0;
		int length = word.length();
		while((curIndex = doc.indexOf(word, startIndex))!=-1){
			count ++;
			startIndex=curIndex + length;
		}
		return count;
	}
	
	@Resource(name="lexiconDao")
	private LexiconDao lexiconDao;
	private static List<String> words = null;
	private static Logger logger = Logger.getLogger(QuestionServiceImpl.class);
}
