package iqaa.xxzh.msl.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import iqaa.xxzh.common.bean.UserBean;

@Component(value="userDao")
public class UserDaoImp extends BaseDao<UserBean> implements UserDao {

	@Override
	public UserBean findById(String userId) {
		List<?> userBeans = getHibernateTemplate().find("from UserBean where userId = ?", userId);
		if(userBeans.isEmpty())
			return null;
		else
			return (UserBean) userBeans.get(0);
	}

	

}
