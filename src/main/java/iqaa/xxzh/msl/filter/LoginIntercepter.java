package iqaa.xxzh.msl.filter;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.service.UserService;
import iqaa.xxzh.msl.utils.Constant;

public class LoginIntercepter extends MethodFilterInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginIntercepter.class);

	@Override
	protected String doIntercept(ActionInvocation arg0) throws Exception {
		Object action = (ActionSupport) arg0.getAction();
		String actionName = action.getClass().getName();
		log.info("拦截器拦截：" + actionName);
		HttpSession session = ServletActionContext.getRequest().getSession();
		UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
		if (user == null) {
			log.info("用户未登录....");
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			HttpSession httpSession = ServletActionContext.getRequest().getSession();
			if (cookies != null)
				for (Cookie c : cookies) {
					String name = c.getName();
					if ("autoLogin".equals(name)) {
						log.info("用户自动登录系统开始......");
						String[] split = c.getValue().split("&&");
						// 获取Spring的SessionFactory
						ServletContext app = ServletActionContext.getServletContext();
						WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(app);

						// 获取服务层对象
						UserService userService = (UserService) ac.getBean("userService");
						// 查询用户信息
						DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class)
								.add(Restrictions.eq("username", split[0]));
						List<? extends Object> list = userService.getList(criteria);
						if (!list.isEmpty()) {
							UserBean user2 = (UserBean) list.get(0);
							// 比较用户密码
							if (user2.getPassword().equals(split[1]) && !user2.getIsLock()) {
								@SuppressWarnings("unchecked")
								Map<UserBean, HttpSession> map = (Map<UserBean, HttpSession>) app
										.getAttribute(UserBean.USER_MAP);
								HttpSession httpSession2 = map.get(user2);
								if (httpSession2 == null) {
									session.setAttribute(Constant.SESSION_USER, user2);
									map.put(user2, httpSession);
									log.info("自动登录成功！");
									return arg0.invoke();
								}
								log.info("该用户已登录，自动登录失败.....");
							}
						}
					}
				}
			return Action.LOGIN;
		} else {
			return arg0.invoke();
		}
	}

}
