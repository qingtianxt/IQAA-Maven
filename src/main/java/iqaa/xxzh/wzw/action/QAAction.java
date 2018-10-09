package iqaa.xxzh.wzw.action;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import iqaa.xxzh.msl.bean.Answer;
import iqaa.xxzh.wzw.domain.Aanswer;
import iqaa.xxzh.wzw.service.QAService;

/**
 * android和后台交互
 * @author wzw
 *
 */
public class QAAction {
	private QAService qaService;
	private String info;
	
	

	public void setInfo(String info) {
		this.info = info;
	}

	public void setQaService(QAService qaService) {
		this.qaService = qaService;
	}
	
	public String getAnswer(){
		try {
			ServletActionContext.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//String info =(String) ServletActionContext.getRequest().getAttribute("info");
		//先获取问题
		if(null!=info){
			Answer answer=qaService.getByQuestion(info);
			Aanswer aanswer = new Aanswer();
			String json =null;
			if(null!=answer){
				
				aanswer.setCode(answer.getId().intValue());
				aanswer.setText(answer.getContent());
				Gson gson =new Gson();
				json = gson.toJson(aanswer);
			}
			try {
				
				 HttpServletResponse response = ServletActionContext.getResponse();
				 response.setCharacterEncoding("utf-8");
				 response.getWriter().println(json);
				 response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}
