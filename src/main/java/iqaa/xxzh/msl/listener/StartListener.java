package iqaa.xxzh.msl.listener;

import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.service.UserService;

public class StartListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	private static Logger log = Logger.getLogger(StartListener.class);
	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("系统初始化...");
		ServletContext context = event.getServletContext();
		context.setAttribute(UserBean.USER_MAP, new LinkedHashMap<UserBean,HttpSession>());
		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
		UserService userService = (UserService) ac.getBean("userService");
		userService.checkDefaultUser();
		
		/*Thread thread = new Thread(()->{
			try {
				log.info("初始化C调用接口...");
				NlpirAdapter adapter = new NlpirAdapter();
				Set<String> keyWords = adapter.getKeyWords("初始化C代码调用接口");
				System.out.println(keyWords);
				log.info("初始化C接口结束...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		thread.run();*/
		log.info("系统初始化结束...");
	}

}
