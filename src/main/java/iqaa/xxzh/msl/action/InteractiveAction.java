package iqaa.xxzh.msl.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import iqaa.xxzh.msl.service.InteractiveService;

@Controller("interactiveAction") @Scope("prototype")
public class InteractiveAction extends ActionSupport {

	@Override
	public String execute() throws Exception {
		service.interactive(question);
		return super.execute();
	}
	
	
	private static final long serialVersionUID = 1L;
	
	@Resource(name="interactiveService") private InteractiveService service;
	private String question;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
}
