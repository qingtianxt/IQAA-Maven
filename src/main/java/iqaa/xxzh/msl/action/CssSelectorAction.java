package iqaa.xxzh.msl.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.msl.bean.CssSelector;
import iqaa.xxzh.msl.service.CssService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.utils.HibernateProxyTypeAdapter;
import iqaa.xxzh.msl.vo.CssQueryBean;
import iqaa.xxzh.msl.vo.PageBean;

@Controller 
@Scope("prototype")
public class CssSelectorAction extends ActionSupport implements ModelDriven<CssSelector>,ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533689842467694049L;
	private CssSelector selector;
	private HttpServletResponse response;
	@Resource(name="cssService")
	private CssService cssService;
	@Override
	public CssSelector getModel() {
		selector = new CssSelector();
		return selector;
	}
	
	public String getRule() throws IOException{
		CssSelector cssSelector = cssService.getByid(selector.getId());
		GsonBuilder b = new GsonBuilder();  
		b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);  
		Gson gson = b.create();  
		String json = gson.toJson(cssSelector);
		response.getWriter().print(json);
		return NONE;
	}
	
	public String del() throws IOException{
		try {
			String msg = cssService.delete(selector);
			ServletActionContext.getRequest().setAttribute("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("msg", e.getMessage());
		}
		return list();
	}

	public String save() throws IOException{
		try {
			String msg = cssService.save(selector);
			response.getWriter().print(msg);
			ServletActionContext.getRequest().setAttribute("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("msg", e.getMessage());
		}
		return list();
	}
	private CssQueryBean queryBean;
	public String list() throws IOException{
		
		if(queryBean == null){
			queryBean = new CssQueryBean();
		}
		
		PageBean<CssSelector> list = cssService.getList(queryBean);
		ServletActionContext.getRequest().setAttribute(Constant.PAGEBEAN,list);
		return "list";
	}
	
	@Override
	public void setServletResponse(HttpServletResponse resp) {
		resp.setContentType("text/html;charset=utf-8");
		this.response = resp;
	}

	public CssQueryBean getQueryBean() {
		return queryBean;
	}

	public void setQueryBean(CssQueryBean queryBean) {
		this.queryBean = queryBean;
	}

	
}
