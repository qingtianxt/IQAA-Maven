package iqaa.xxzh.msl.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.DocBeanDao;

/**
 * @author Administrator
 *	调用python进程的questionService
 */
@Service("questionService")
@Transactional
public class QuestionService3Impl extends QuestionServiceImpl {
	
	
	@Resource(name="docDao")
	private DocBeanDao docDao;
	private static Logger log = Logger.getLogger(QuestionService3Impl.class);
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
		
		// 
		List<Question> list = remoteCall(content);
		
		// 设置文档已经分析的标志
		doc.setMark(mark|4);
		return list;
	}
	
	private List<Question> remoteCall(String content){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("content", content);
		String str = jsonObject.toJSONString();
		// 访问服务进程的套接字
		Socket socket = null;
		List<Question> questions = new ArrayList<>();
		log.info("调用远程接口:host=>"+HOST+",port=>"+PORT);
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
			JSONArray array = JSON.parseArray(sb.toString());
			int size = array.size();
			for(int i = 0; i < size; i++){
				JSONObject obj = array.getJSONObject(i);
				String question = obj.getString("question");
				String answer = obj.getString("answer");
				Question q = new Question();
				q.setContent(question);
				Answer a = new Answer();
				a.setContent(answer);
				q.setStandardAnswer(a);
				questions.add(q);
			}
			return questions;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {if(socket!=null) socket.close();} catch (IOException e) {}
			log.info("远程接口调用结束.");
		}
		return null;
	}
	private static String HOST = null;
	private static int PORT = 0;
	static{
		InputStream is = QuestionService3Impl.class.getClassLoader().getResourceAsStream("db.properties");
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
}
