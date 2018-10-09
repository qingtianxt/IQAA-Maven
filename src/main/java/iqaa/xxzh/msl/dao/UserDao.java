package iqaa.xxzh.msl.dao;

import iqaa.xxzh.common.bean.UserBean;

public interface UserDao extends Dao<UserBean> {

	UserBean findById(String userId);

}
