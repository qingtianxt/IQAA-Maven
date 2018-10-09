package iqaa.xxzh.msl.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.vo.PageBean;
import iqaa.xxzh.msl.vo.UserQueryInfo;

public interface UserService extends Serializable {

	void saveOrUpdate(UserBean userBean);

	List<UserBean> getList();

	List<? extends Object> getList(DetachedCriteria criteria);

	String login(UserBean u);

	void checkDefaultUser();

	void addUser(UserBean userBean);

	PageBean<UserBean> getPage(UserQueryInfo query);

	void lockUser(String userId);

	void delete(String userId);

	String modifyPwd(UserBean userBean);
}
