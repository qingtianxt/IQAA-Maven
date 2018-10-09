package iqaa.xxzh.msl.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.msl.service.AnswerService;
import iqaa.xxzh.msl.service.KbService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.vo.AnswerQueryInfo;
import iqaa.xxzh.msl.vo.PageBean;

@Controller("kbAction") @Scope("prototype")
public class KbAction extends ActionSupport implements ModelDriven<Answer>,ServletRequestAware,ServletResponseAware{
	
	
	public String get(){
		Answer answer2 = answerService.getAnswer(answer.getId());
		try {
			BeanUtils.copyProperties(answer2, answer);
		} catch (BeansException e) {
			e.printStackTrace();
		}
		return "showdetail";
	}
	
	public String toUpdate(){
		Answer answer2 = answerService.getAnswer(answer.getId());
		request.setAttribute("answer", answer2);
		return "update";
	}
	
	public String update(){
		boolean b = kbService.update(answer);
		request.setAttribute("msg", b?"修改成功！":"修改失败！");
		return list();
	}
	/**
	 * 删除
	 * @return
	 */
	public String delete(){
		try {
			this.kbService.delete(this.answer);
			request.setAttribute("msg", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
		}
		return this.list();
	}
	public String del() throws IOException{
		PrintWriter out = response.getWriter();
		try {
			kbService.delete(answer);
			out.print("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			out.print(e.getMessage());
		}
		return NONE;
	}
	public String add() throws IOException{
		PrintWriter out = response.getWriter();
		try {
			UserBean user = (UserBean) request.getSession().getAttribute(Constant.SESSION_USER);
			answer.setOperator(user);
			this.kbService.add(answer);
			out.write("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("添加失败！");
		}
		return NONE;
	}

	public String list(){
		
		HttpSession session = request.getSession();
		if(query==null){
			query = (AnswerQueryInfo) session.getAttribute(AnswerQueryInfo.SESSION_ANSERQUERYINFO);
			if(query == null){
				query = new AnswerQueryInfo();
			}
		}
		session.setAttribute(AnswerQueryInfo.SESSION_ANSERQUERYINFO, query);
		try {
			PageBean<Answer> pageBean = kbService.getList(query);
			request.setAttribute(Answer.REQUEST_PAGEBEAN, pageBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	@Resource(name="kbService")
	private KbService kbService;
	@Autowired private AnswerService answerService;
	private Answer answer;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private AnswerQueryInfo query;
	private static final long serialVersionUID = -7873516864170537281L;
	public Answer getModel() {
		answer = new Answer();
		return answer;
	}
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
		this.response.setCharacterEncoding("UTF-8");
	}
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
		
	}


	public AnswerQueryInfo getQuery() {
		return query;
	}


	public void setQuery(AnswerQueryInfo query) {
		this.query = query;
	}

}
