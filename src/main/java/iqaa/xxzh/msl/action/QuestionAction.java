package iqaa.xxzh.msl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.service.AnswerService;
import iqaa.xxzh.msl.service.QuestionService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.utils.HibernateProxyTypeAdapter;

@Controller @Scope("prototype")
public class QuestionAction extends ActionSupport implements ModelDriven<DocBean> {

	/**
	 * 通过模型，使用深度学习分析
	 * selwimo：select with model
	 * @return
	 * @throws IOException 
	 */
	public String selwimo() throws IOException{
		log.info("使用模型分析.....");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
		docBean.setUserBean(user);
		List<Question> list = null;
		JsonObject jsonObject = new JsonObject();
		String msg = null;
		try{
			list = service.selectbynn(docBean);
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
			Gson gson = builder.create();
			JsonElement data = gson.toJsonTree(list);
			msg = "success";
			jsonObject.add("data", data);
		}catch(AnalyzedException e){
			msg = e.getMessage();
		}
		jsonObject.addProperty("msg", msg);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(jsonObject);
		return NONE;
	}
	/**
	 * 页面传递某个文档的ID，分析指定ID的文档中的问题-答案组合,
	 * 访问该链接的页面：/document/showDetail.jsp
	 * @return ‘none’
	 * @throws IOException
	 */
	public String select() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
		docBean.setUserBean(user);
		// 分析文档，按照规则解析差问题-答案组合
		List<Question> list = null;
		JsonObject jsonObject = new JsonObject();
		String msg = null;
		try {
			list = service.select(docBean);
			GsonBuilder b = new GsonBuilder();  
			b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);  
			Gson gson = b.create();  
			jsonObject.add("data", gson.toJsonTree(list));
			msg = "success";
		} catch (AnalyzedException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		// 将结果发送到浏览器
		jsonObject.addProperty("msg", msg);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(jsonObject);
		return NONE;
	}
	
	/**
	 * 批量处理文档提取问题,访问页面：
	 * /document/list_doc.jsp
	 * @return
	 * @throws Exception 
	 */
	public String batchselect() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
		List<Question> totallist = new ArrayList<>();
		// 子线程计数器，为每个文档开启一个子线程
		CountDownLatch countDownLatch = new CountDownLatch(docBeans.size());
		for (DocBean docBean : docBeans) {
			new Thread(()->{
				log.info("开始分析文档:"+docBean.getId());
				try {
					docBean.setUserBean(user);
					List<Question> list = service.select(docBean);
					totallist.addAll(list);
				} catch (Exception e) {
					e.printStackTrace();
					addActionError(e.getMessage());
				} finally {
					// 计时器减一
					countDownLatch.countDown();
					log.info(docBean.getId()+"号文档分析完成！");
				}
			}).start();
		}
		//等待所有子线程结束
		countDownLatch.await();
		request.setAttribute(Constant.QUESTIONS, totallist);
		setCookie(totallist);
		return "batchselect";
	}

	/**
	 * 将此次分析的结果添加到cookie中
	 * @param totallist
	 */
	private void setCookie(List<Question> totallist) {
		StringBuffer sb = new StringBuffer();
		for (Question question : totallist) {
			sb.append(question.getId()).append(',');
		}
		if(!StringUtils.isEmpty(sb))
			sb.deleteCharAt(sb.length()-1);
		Cookie cookie = new Cookie("backselect",sb.toString());
		HttpServletRequest request = ServletActionContext.getRequest();
		cookie.setPath(request.getContextPath()+"/kb/backselectquestion.action");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.addCookie(cookie);
	}
 
	public String content(){
		return "detail";
	}

	private Cookie getCookie(String cookieName) {
		HttpServletRequest request = ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			if(cookieName.equals(name)){
				return cookie;
			}
		}
		return null;
	}
	
	/**
	 * 返回分析结果列表页
	 * @return
	 */
	public String backselect(){
		// 获取保存在cookie中的分析结果
		Cookie cookie = getCookie("backselect");
		if(cookie!=null){
			String value = cookie.getValue();//Base64EncoderUtils.decoding(cookie.getValue());
			List<Question> list = this.service.backselect(value);
			setCookie(list);
			ServletActionContext.getRequest().setAttribute(Constant.QUESTIONS, list);
		}
		return "backselect";
	}
	
	
	public String show(){
		Answer answer = answerService.getAnswer(question.getStandardAnswer().getId());
		ActionContext.getContext().getValueStack().push(answer);
		return "show";
	}
	
	
	public DocBean getModel() {
		docBean = new DocBean();
		return docBean;
	}
	private static Logger log = Logger.getLogger(QuestionAction.class);
	private DocBean docBean;
	private List<DocBean> docBeans = null;
	@Autowired private QuestionService service;
	@Autowired private AnswerService answerService;
	private static final long serialVersionUID = 1L;
	
	private Question question;
	
	public List<DocBean> getDocBeans() {
		return docBeans;
	}

	public void setDocBeans(List<DocBean> docBeans) {
		this.docBeans = docBeans;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
}
