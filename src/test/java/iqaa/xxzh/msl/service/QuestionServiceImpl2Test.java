package iqaa.xxzh.msl.service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.dao.UserDao;


public class QuestionServiceImpl2Test {
	
	private QuestionService questionService;
	private UserDao userDao;
	
	@Before
	public void setUp(){
		ApplicationContext app = new ClassPathXmlApplicationContext("beans.xml");
		questionService = (QuestionService) app.getBean("questionService");
		userDao = (UserDao) app.getBean("userDao");
		
	}
	@Test
	public void testSelectbynn() {
		DocBean docBean = new DocBean();
		docBean.setId(8l);
		UserBean user = userDao.findById("8a99060b6493636e01649363787e0001");
		docBean.setUserBean(user);
		long start = System.currentTimeMillis();
		List<Question> q = questionService.selectbynn(docBean);
		long end = System.currentTimeMillis();
		for (Question question : q) {
			System.out.println(question);
		}
		System.out.println("好费时间"+(end-start)+"ms");
	}

	@Test
	public void testText2vec() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringCount() {
		fail("Not yet implemented");
	}

}
