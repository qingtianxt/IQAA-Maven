package iqaa.xxzh.msl.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.common.bean.Role;
import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.service.UserService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.vo.PageBean;
import iqaa.xxzh.msl.vo.UserQueryInfo;

@Component("manageAction") @Scope("prototype")
public class ManageAction extends ActionSupport implements ModelDriven<UserBean> {

	/**
	 * 重置密码
	 * @return
	 */
	public String resetpwd(){
		HttpServletRequest request = ServletActionContext.getRequest();
		// 获取当前登录的用户
		UserBean userBean = (UserBean) request.getSession().getAttribute(Constant.SESSION_USER);
		// 获取登录用户的角色
		Role role = userBean.getRole();
		// 当前用户是否是超级管理员
		boolean equals = role.getRolename().equals("超级管理员");
		if(equals){
			// 修改指定用户的密码为123456
			this.userBean.setPassword("123456");
			// 保存到数据库
			String msg = userService.modifyPwd(this.userBean);
			// 修改成功
			request.setAttribute(Constant.MSG, msg);
		}else{
			// 权限不够
			request.setAttribute(Constant.MSG, "您没有权限！");
		}
		// 显示用户列表
		return showUser();
	}
	
	/**
	 * 修改用户密码
	 */
	public String modifyPwd(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			// 获取当前登录的用户
			UserBean userBean = (UserBean) request.getSession().getAttribute(Constant.SESSION_USER);
			// 获取页面传递的旧密码
			String oldpwd = request.getParameter("oldpwd");
			// 比较当前用户的密码与页面传递的密码是否一致
			if(userBean.getPassword().equals(oldpwd)){
				// 为当前用户设置信息密码
				userBean.setPassword(this.userBean.getPassword());
				// 将新密码更新到数据库中
				String msg = this.userService.modifyPwd(userBean);
				// 更新成功
				request.setAttribute(Constant.MSG, msg);
			}else{
				// 密码错误
				request.setAttribute(Constant.MSG, "密码错误");
			}
		} catch (Exception e) {
			// 修改失败
			request.setAttribute(Constant.MSG, "修改失败！");
		}
		return "modify";
	}
	
	public String logout(){
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute(Constant.SESSION_USER);
		return LOGIN;
	}
	
	public String delete(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
			userService.delete(userBean.getUserId());
			request.setAttribute("msg", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "删除失败，请稍后重试！");
		}
		return showUser();
	}
	
	
	public String lock(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
			// 锁定用户
			userService.lockUser(userBean.getUserId());
			request.setAttribute("msg", "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "修改失败，请稍后重试！");
		}
		return showUser();
	}
	
	/**
	 * 显示用户列表
	 * @return
	 */
	public String showUser(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		if(this.query == null){// 没有传递查询参数
			// 从session中获取上次查询的参数
			query = (UserQueryInfo) session.getAttribute(UserQueryInfo.SEESSION_QUERY_INFO);
			if(query == null){
				// 上次查询参数也为null，设置查询参数
				query = new UserQueryInfo();
				query.setCurrentPage(1);
				query.setPageSize(5);
			}
		}
		// 将查询参数保存到session中
		session.setAttribute(UserQueryInfo.SEESSION_QUERY_INFO, query);
		try {
			// 调用业务逻辑查询到相关信息
			PageBean<UserBean> page = userService.getPage(query);
			request.setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	public String addUser() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			// 保存用户
			userService.addUser(userBean);
			// 保存成功
			request.setAttribute("msg", "添加成功！");
		} catch (Exception e) {
			// 保存失败
			request.setAttribute("msg", "添加失败，请稍后重试！");
		}
		return "toList";
	}
	
	public String sign_in() throws IOException{
		String msg = login();
		if("login fail".equals(msg)){
			List<String> list = getFieldErrors().get("loginMsg");
			if(!list.isEmpty()){
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print(list.get(0));
			}
		}
		return NONE;
	}
	
	public String login(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		// 根据用户密码查找用户
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class).add(Restrictions.eq("username", userBean.getUsername()));
		List<? extends Object> list = userService.getList(criteria);
		
		if(!list.isEmpty()){
			// 用户存在
			UserBean user = (UserBean) list.get(0);
			// 比较用户密码是否一致
			if(userBean.getPassword().equals(user.getPassword())){
				// 如果用户密码一致
				HttpSession session = request.getSession();
				// 将用户保存到session当中
				session.setAttribute(Constant.SESSION_USER, user);
				// 获取容器上下文变量
				ServletContext app = request.getServletContext();
				@SuppressWarnings("unchecked")
				// 获取在容器启动时设置的map变量，该变量中服务器启动的是时候通过
				// 监听器保存到了容器中，该map的键是用户对象，值是session对象
				Map<UserBean,HttpSession> map = 
					(Map<UserBean, HttpSession>) app.getAttribute(UserBean.USER_MAP);
				// 根据当前登录对象从容器中获取session对象
				HttpSession httpSession = map.get(user);
				
				if(session != httpSession && httpSession != null)
					// 说明该用户已经在某地登录了系统，将它的会话关闭
					httpSession.invalidate();
				
				// 将用户的session保存到容器
				map.put(user, session);
				if(isAutoLogin()){// 用户选择了自动登录
					// 添加cookie保存用户名和密码到浏览器中
					Cookie cookie = new Cookie(
							"autoLogin", 
							userBean.getUsername()
							+"&&"+userBean.getPassword());
					cookie.setMaxAge(30*24*3600);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				return "login success";
			}
			else{
				// 密码错误
				addFieldError("loginMsg", "密码错误！");
			}
		}
		else{
			// 用户名不存在
			addFieldError("loginMsg", "用户名不存在！");
		}
		
		return "login fail";
	}
	
	private static final long serialVersionUID = 1L;
	
	private UserQueryInfo query;
	
	@Resource(name="userService")
	private UserService userService;
	@Override
	public UserBean getModel() {
		return userBean;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	private UserBean userBean = new UserBean();
	
	private boolean autoLogin = false;
	public UserQueryInfo getQuery() {
		return query;
	}


	public void setQuery(UserQueryInfo query) {
		this.query = query;
	}
}
