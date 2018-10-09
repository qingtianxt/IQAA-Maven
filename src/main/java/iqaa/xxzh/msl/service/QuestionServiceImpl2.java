package iqaa.xxzh.msl.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;

import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.DocBeanDao;
import iqaa.xxzh.msl.dao.LexiconDao;

@Deprecated
public class QuestionServiceImpl2 extends QuestionServiceImpl {
	private static String HOST = null;
	private static int PORT = 0;
	static{
		InputStream is = QuestionServiceImpl2.class.getClassLoader().getResourceAsStream("db.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
			HOST = prop.getProperty("sockethost");
			PORT = Integer.parseInt(prop.getProperty("socketport"));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Resource(name="docDao")
	private DocBeanDao docDao;
	
	@Resource(name="lexiconDao")
	private LexiconDao lexiconDao;
	@Override
	public List<Question> selectbynn(DocBean docBean) {
		DocBean doc = docDao.findById(docBean.getId());
		if(doc==null)
			throw new RuntimeException("没有ID为"+docBean.getId()+"的文档..");
		// 检查文档是否已经分析过
		int mark = doc.getMark();
		if((mark & 4 ) > 0)
			throw new AnalyzedException(doc);
		// 文本内容
		String content = doc.getContent();
		Elements elements = Jsoup.parse(content).select("div.content-block");
		StringBuffer pertext = new StringBuffer();
		for (Element element : elements) {
			pertext.append(element.text());
		}
		// 构造HTML标签的正则表达式
		String regex_style = "<style [^>]*?>[\\s\\S]*?<\\/style>";
		String regex_script = "<script[^>]*?[\\s\\S]*?<\\/script>";
		String regex_html = "<[^>]+>";
		// 去除所有HTML标签
		String[] split = 
				content.split("("+regex_style+")|("+regex_script+")|("+regex_html+")");
		List<String> words = lexiconDao.getLexicons();
		List<String> tmpstrs=new LinkedList<>();
		// 存放提取的问题的数组
		List<Question> questions = new ArrayList<>(split.length);
		// 问题向量矩阵
		List<List<Integer>> rectangle = new ArrayList<>(split.length);
		for (String clip : split) {
			if(!StringUtils.isEmptyOrWhitespaceOnly(clip)){
				String trim = clip.trim();
//				pertext.append(trim);
				tmpstrs.add(trim);
				Question q = new Question();
				q.setContent(trim);
				q.setClickRate(0l);
				q.setDoc(doc);
				q.setOperator(docBean.getUserBean());
				q.setAddtime(new Date());
				questions.add(q);
				rectangle.add(text2vec(trim, words));
			}
		}
		// 文章向量
		String article = pertext.toString();
		List<Integer> av = text2vec(article,words);
		// 最终结果数组
		List<Question> qs = new ArrayList<>();
		try {
			List<Integer> result = remoteCall(rectangle,av);
//			System.out.println(result);
			int index = 0;
			for (Integer f : result) {
				// 如果预测结果为true
				if(f==0){
					Question question = questions.get(index);
					String text = question.getContent();
					int indexOf = article.indexOf(text);
					if(indexOf!=-1){
						// 截取该问提的上下文构造一个答案
						int start = indexOf > 100 ? indexOf - 100 : 0;
						int end = indexOf < pertext.length() - 100 ? indexOf + 100:pertext.length();
						CharSequence answerText = article.subSequence(start, end);
						// 设置答案
						Answer answer = new Answer();
						answer.setAddtime(new Date());
						answer.setDoc(doc);
						answer.setContent(answerText.toString());
						answer.setOperator(docBean.getUserBean());
						question.setStandardAnswer(answer);
						qs.add(question);
					}
				}
				index ++;
			}
//			System.out.println(qs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 设置文档已经分析的标志
		doc.setMark(mark|4);
		return qs;
	}

	/**
	 * 远程调用python进程，使用神经网络模型获取判断结果
	 * @param rectangle	问题转化的词向量
	 * @param av		文章词向量
	 * @return			分析结果
	 * @throws IOException 
	 */
	private List<Integer> remoteCall(List<List<Integer>> rectangle, List<Integer> av) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("questions", rectangle);
		jsonObject.put("article", av);
		String str = jsonObject.toJSONString();
		Logger log = Logger.getLogger(getClass());
		log.info("调用远程接口:host=>"+HOST+",port=>"+PORT);
//		System.out.println(str);
		// 访问服务进程的套接字
		Socket socket = null;
		try {
			// 初始化套接字，设置访问服务的主机和进程端口号
			socket = new Socket(HOST,PORT);
			// 获取输出流对象
			OutputStream os = socket.getOutputStream();
			PrintStream out = new PrintStream(os);
			// 发送内容
			out.print(str);
			// 告诉服务进程，内容发送完毕，可以开始处理
			out.print("over");
			// 获取服务进程的输入流
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String tmp = null;
			StringBuilder sb = new StringBuilder();
			// 读取内容
			while((tmp=br.readLine())!=null)
				sb.append(tmp).append('\n');
			// 解析结果
			List<Integer> parseArray = JSON.parseArray(sb.toString(), Integer.class);
			return parseArray;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket!=null)
				socket.close();
			log.info("远程接口调用结束.");
		}
		return null;
	}

	public List<Integer> text2vec(String trim, List<String> words) {
		List<Integer> vector = new ArrayList<>(words.size());
		for (String word : words) {
			int times = stringCount(trim, word);
			vector.add(times);
		}
		return vector;
	}
	
	/**
	 * 统计字符串str中的子字符串key出现的次数
	 * @param str
	 * @param key
	 * @return	key在str中出现的次数
	 */
	public static int stringCount(String str, String key) {
		int index = 0;
		int count = 0;
		str = "\n"+str;
		while ((index = str.indexOf(key,index+1)) != -1) {
//			str = str.substring(index + key.length());
			count++;
		}

		return count;
	}
}
