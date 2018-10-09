package iqaa.xxzh.msl.service;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.adapter.JcseAdapter;
import iqaa.xxzh.msl.adapter.TokenizerAdapter;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Lexicon;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.DocBeanDao;
import iqaa.xxzh.msl.dao.LexiconDao;
import iqaa.xxzh.msl.dao.QuestionDao;
import iqaa.xxzh.msl.utils.FileParser;
import iqaa.xxzh.msl.vo.DocQueryInfo;
import iqaa.xxzh.msl.vo.PageBean;

@Component("docService")
@Transactional
public class DocServiceImpl implements DocService {

	private static final long serialVersionUID = 1L;
	@Resource(name="docDao") 
	private DocBeanDao docDao;
	@Resource(name="questionDao") 
	private QuestionDao questionDao;
	
	@Resource(name="lexiconDao")
	private LexiconDao lexiconDao;
	
	private static Logger log = Logger.getLogger(DocServiceImpl.class);
	
	@Deprecated
	public boolean saveFile(DocBean doc) {
		docDao.saveOrUpdate(doc);
		return true;
	}
	@Override
	public PageBean<DocBean> getList(DocQueryInfo queryInfo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocBean.class).add(Restrictions.eqOrIsNull("isDelete", false));
		String param = queryInfo.getParam();
		if(param!=null){
			String text = param.trim();
			criteria.add(Restrictions.like("docName", "%"+text+"%"));
		}
		Date start = queryInfo.getStart();	
		if(start!=null){
			criteria.add(Restrictions.ge("addtime", start));
		}
		Date end = queryInfo.getEnd();
		if(end!=null){
			criteria.add(Restrictions.le("addtime", end));
		}
		criteria.addOrder(Order.desc("addtime"));
		PageBean<DocBean> list = docDao.findByCriteria(criteria, queryInfo.getCurrentPage(), queryInfo.getPageSize());
		return list;
	}
	@Override
	public void delete(DocBean doc) {
		doc = docDao.findById(doc.getId());
		doc.setIsDelete(true);
		docDao.saveOrUpdate(doc);
	}
	@Override
	public DocBean getById(Long id) {
		DocBean docBean = docDao.findById(id);
		return docBean;
	}
	@Override
	public void saveFile(List<DocBean> list) {
		// 文档保存队列
		List<DocBean> saveList = new ArrayList<>();
		// 遍历待保存的文档数组
		for (DocBean docBean : list) {
			// 获取文档标题
			String docName = docBean.getDocName();
			// 根据标题查找文档
			DocBean doc = docDao.findByName(docName);
			// 如果文档不存在，将这片文旦添加到保存队列中
			if(doc==null)
				saveList.add(docBean);
		}
		// 保存文档队列
		docDao.saveOrUpdate(saveList);
		// 跟新词库词频
		updateFrequency(saveList);
		list.clear();
		list.addAll(saveList);
	}
	/**
	 * 根据文档列表跟新词语库中的词频
	 * @param saveList
	 */
	public void updateFrequency(List<DocBean> saveList) {
		// 词库列表
		List<Lexicon> words = null;
		
		synchronized(this){
			words = lexiconDao.getWords();
		}
		// 计时器开始时间
		long startTime = System.currentTimeMillis();
		// 词库大小
		int size = words.size();
		// 分组长度
		int ql = size%1000==0? size/1000:size/1000+1;
		// 线程计数器
		CountDownLatch countDownLatch = new CountDownLatch(ql);
		// 遍历文档跟新词库
		for (DocBean doc : saveList) {
			// 为没1000个词库开启一个线程
			for(int i =0;i < words.size(); i+=1000){
				Thread thread = new Thread(new UpdateWordsTimes(doc,words,i,1000,countDownLatch));
				thread.start();
			}
		}
		// 等待所有线程结束
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info(String.format("更新词频耗时%dms..",System.currentTimeMillis()-startTime));
	}
	@Override
	public String getDocContent(DocBean doc) {
		String path = doc.getPath();
		path = ServletActionContext.getServletContext().getRealPath(path);
		String text = null; 
		try {
			path = URLDecoder.decode(path, Charset.defaultCharset().name());
			FileParser parser = FileParser.newInstance(path);
			text = parser.getText();
			//String tempString = parser.getText(is);
			//tempString = new String(tempString.getBytes(),"UTF-8");
			text = text.replaceAll("\n", "。<br/>\n");
		} catch (Exception e) {
			e.printStackTrace();
			text = "该文档暂不能查看详情";
		}
		return text;
	}
	@Override
	public void analysis(InputStream is) throws Exception {
		Document htmlDoc = Jsoup.parse(is,"UTF-8","");
		Elements aTags = htmlDoc.getElementsByTag("A");
		for (Element aTag : aTags) {
			String html = aTag.html();
			String attr = aTag.attr("href");
			log.info(html);
			log.info(attr);
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see iqaa.xxzh.msl.service.DocService#extract(java.io.File)
	 */
	public List<Answer> extract(File file){
		throw new RuntimeException("请使用extract(DocBean doc)代替");
	}
	/**
	 * 分词适配器adapter从summary中提取出若干关键词作为备选问题，summary作为答案，生成一个存放答案的JavaBean中
	 * 添加到list集合中
	 * @param list
	 * @param adapter
	 * @param summary
	 */
	private void generator(List<Question> list, TokenizerAdapter adapter, String summary) {
		log.info("提取语句{"+summary+"}中的关键字词语...");
		Set<String> keyWords = adapter.getKeyWords(summary);//getKeyPhrase(summary);
		log.info("获取关键词语"+keyWords.size()+"个...");
		keyWords.forEach(item->{
			Question question = new Question();
			Answer answer = new Answer();
			answer.setContent(summary);
			question.setContent(item);
			question.setStandardAnswer(answer);
			list.add(question);
		});
	}
	@Override
	public List<Answer> extractByRegulation(DocBean file) {
		/*DocBean docBean = docDao.findById(file.getId());
		List<Answer> list = new LinkedList<Answer>();
		String content = docBean.getContent();
		getAnswer(list, content);
		try {
			BeanUtils.copyProperties(docBean, file);
		} catch (BeansException e) {
		}
		return list;*/
		throw new RuntimeException("该方法已过时，请使用QuestionService.select()方法");
	}
	@Deprecated
	protected void getAnswer(List<Answer> list, CharSequence contentsAsString) {
		// 构造HTML标签的正则表达式
		String regex_style = "<style [^>]*?>[\\s\\S]*?<\\/style>";
		String regex_script = "<script[^>]*?[\\s\\S]*?<\\/script>";
		String regex_html = "<[^>]+>";
		// 去除所有HTML标签
		String[] split = 
				contentsAsString.toString().split("("+regex_style+")|("+regex_script+")|("+regex_html+")");
		StringBuffer sb = new StringBuffer();
		int length = split.length;
		int j = 0;
		String ans = null;
		for(int i = 0; i < length; i++){
			if(split[i].trim().isEmpty())
				continue;
			sb.append(split[i]);
			j ++;
			if(j % 3 == 0){
				ans = split[i];
			}
			if(j % 5 == 0){
				Answer answer = new Answer();
				answer.setContent(sb.toString());
				Question question = new Question();
				question.setContent(ans);
				answer.setStandardQuestion(question);
				list.add(answer);
				sb.delete(0, sb.length()-1);
				j = 0;
			}
		}
	}
	@Override
	public List<Question> extract(DocBean doc) {
		DocBean docBean = docDao.findById(doc.getId());
		if(docBean==null)
			throw new RuntimeException("指定id的文档不存在");
		int mark = docBean.getMark();
		if(!(mark==0||mark==1||mark==4||mark==5))
			throw new AnalyzedException(docBean);
		String content = docBean.getContent();
		// 构造HTML标签的正则表达式
		String regex_style = "<style [^>]*?>[\\s\\S]*?<\\/style>";
		String regex_script = "<script[^>]*?[\\s\\S]*?<\\/script>";
		String regex_html = "<[^>]+>";
		// 去除所有HTML标签
		String[] split = 
				content.split("("+regex_style+")|("+regex_script+")|("+regex_html+")");
		StringBuffer sb = new StringBuffer();
		for (String item : split) {
			sb.append(item).append("\n");
		}
		String text = sb.toString();
		try {
			List<Question> list = new LinkedList<>();
			// 根据文件路径名称判断文件类型，获取相应类型的文件解析器
			// 读取文件中的纯文本字符串
			// 分词解析适配器
			JcseAdapter adapter = new JcseAdapter();
			// 提取关键语句
			log.info("提取关键语句...");
			Set<String> sentence = adapter.getKeySentence(text);
			log.info("一共提取到关键语句："+sentence.size()+"条");
			if(sentence.isEmpty()){
				String summary = adapter.getSummary(text, 200);
				generator(list, /*new NlpirAdapter()*/adapter, summary);
			}else{
				sentence.forEach(summary -> {
					generator(list,/*new NlpirAdapter()*/adapter,summary);
				});
			}
			for (Question q : list) {
				q.setOperator(doc.getUserBean());
			}
			questionDao.saveOrUpdate(list);
			doc.setMark(mark|2);
			docDao.saveOrUpdate(docBean);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * @author Administrator
	 * 更新词语出现的在文当中出现的次数的子线程类
	 */
	class UpdateWordsTimes implements Runnable{
		
		private DocBean doc;
		private List<Lexicon> words;
		private int start;
		private int size;
		private CountDownLatch countDownLatch;
		
		/**
		 * 构造一个子线程，子线程的主要功能是查看doc对象的文档内容中是否包含词组列表list从start下标开始的size个词组
		 * @param doc		封装了一个文档对象
		 * @param list		词组列表
		 * @param start		词组列表的开始检索位置
		 * @param size		检索词语个数
		 */
		public UpdateWordsTimes(final DocBean doc,final List<Lexicon> list,int start,int size,CountDownLatch countDownLatch){
			this.doc = doc;
			this.words = list;
			this.start = start;
			this.size = size;
			this.countDownLatch = countDownLatch;
		}
		@Transactional
		public void run() {
			long before = System.currentTimeMillis();
			try {
				// 获取词表长度
				int length = this.words.size();
				// 从start开始循环size次
				for (int i = start; i<start + size && i < length; i++){
					// 获取一个词组
					Lexicon lexicon = words.get(i);
					// 查看当前文档中是否包含有这个词组
					boolean contanis = doc.getContent().contains(lexicon.getWord());
					// 如果有
					if(contanis){
						// 更新词组在文档中出现的次数
						int times = lexicon.getTimes();
						times++;
						lexicon.setTimes(times);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.countDownLatch.countDown();
			}
			long end = System.currentTimeMillis();
			log.info(String.format("分析文档‘%s’的%d号更新文档词频子线程执行完毕，耗时%dms",doc.getDocName(), start/size+1,end-before));
		}
	}
}













