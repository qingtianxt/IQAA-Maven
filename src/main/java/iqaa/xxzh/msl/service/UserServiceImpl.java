package iqaa.xxzh.msl.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.common.bean.Role;
import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.dao.RoleDao;
import iqaa.xxzh.msl.dao.UserDao;
import iqaa.xxzh.msl.vo.PageBean;
import iqaa.xxzh.msl.vo.UserQueryInfo;

@Component(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "roleDao")
	private RoleDao roleDao;

	@Override
	public void saveOrUpdate(UserBean userBean) {
		userDao.saveOrUpdate(userBean);
	}

	@Override
	public List<UserBean> getList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class);
		List<UserBean> list = userDao.findByCriteria(criteria, UserBean.class);
		return list;
	}

	@Override
	public List<? extends Object> getList(DetachedCriteria criteria) {
		List<UserBean> list = userDao.findByCriteria(criteria, UserBean.class);
		return list;
	}

	@Override
	public String login(UserBean u) {
		String username = u.getUsername();
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class)
				.add(Restrictions.eq("username", username));
		List<UserBean> user = userDao.findByCriteria(criteria, UserBean.class);
		log.info(user);
		return null;
	}

	private static Logger log = Logger.getLogger(UserServiceImpl.class);

	@Override
	public void checkDefaultUser() {
		log.info("检查超级管理员admin用户是否存在....");
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class).add(Restrictions.eq("username", "admin"));
		List<UserBean> list = userDao.findByCriteria(criteria, UserBean.class);
		if (list.isEmpty()) {
			log.info("用户列表为空，添加超级管理员...");
			UserBean userBean = new UserBean();
			userBean.setAddtime(new Date());
			userBean.setIsLock(false);
			userBean.setPassword("admin");
			userBean.setSalt("1");
			userBean.setUsername("admin");
			List<Role> roles = roleDao.findByCriteria(DetachedCriteria.forClass(Role.class), Role.class);
			if (roles.isEmpty()) {
				log.info("角色列表为空，添加角色...");
				Role role = new Role();
				role.setRolename("超级管理员");
				role.setAddtime(new Date());
				roleDao.saveOrUpdate(role);
				userBean.setRole(role);
				log.info("角色添加成功...");
			} else {
				userBean.setRole(roles.get(0));
			}
			userDao.saveOrUpdate(userBean);
			log.info("用户添加成功...");
		}

	}

	@Override
	public void addUser(UserBean userBean) {
		// 根据用户名查找用户信息
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class);
		criteria.add(Restrictions.eq("username", userBean.getUsername()));
		List<UserBean> users = userDao.findByCriteria(criteria, UserBean.class);
		// 如果查询结果不为空，说明该用户名已经存在了
		if(!users.isEmpty())
			throw new RuntimeException("该用户已存在！");
		// 根据角色ID查找角色
		List<Role> roles = roleDao.findByCriteria(
				DetachedCriteria.forClass(Role.class).add(Restrictions.eq("roleid", userBean.getRole().getRoleid())),
				Role.class);
		// 如果角色不存在
		if (roles.isEmpty())
			throw new RuntimeException("角色不存在！");
		Role role = roles.get(0);
		userBean.setRole(role);
		userBean.setAddtime(new Date());
		userBean.setIsLock(true);
		// 保存用户
		userDao.saveOrUpdate(userBean);
	}

	@Override
	public PageBean<UserBean> getPage(UserQueryInfo query) {
		// 根据query中封装的查询信息，拼接查询条件
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBean.class);
		String username = query.getUsername();
		if (username != null && !username.isEmpty())
			criteria.add(Restrictions.like("username", "%" + username + "%"));
		String startTime = query.getStartTime();
		if (startTime != null && !startTime.isEmpty())
			criteria.add(Restrictions.ge("addtime", startTime));
		String endTime = query.getEndTime();
		if (endTime != null && !endTime.isEmpty())
			criteria.add(Restrictions.le("addtime", endTime));
		criteria.addOrder(Order.desc("addtime"));
		// 查询结果
		PageBean<UserBean> pageBean = this.userDao.findByCriteria(criteria, query.getCurrentPage(),
				query.getPageSize());
		return pageBean;
	}

	@Override
	public void lockUser(String userId) {
		// 查找指定ID的用户
		List<UserBean> list = userDao.findByCriteria(
				DetachedCriteria.forClass(UserBean.class).add(Restrictions.eq("userId", userId)), UserBean.class);
		// 用户不存在
		if (list.isEmpty())
			throw new RuntimeException("ID号'" + userId + "'的用户不存在！");
		UserBean userBean = list.get(0);
		Boolean isLock = userBean.getIsLock();
		// 设置用户锁定状态
		userBean.setIsLock(isLock ? false : true);
		// 保存用户
		userDao.saveOrUpdate(userBean);
	}

	public void delete(String userid) {
		// 查找用户
		List<UserBean> list = userDao.findByCriteria(
				DetachedCriteria.forClass(UserBean.class).add(Restrictions.eq("userId", userid)), UserBean.class);
		// 用户不存在
		if (list.isEmpty())
			throw new RuntimeException("ID号'" + userid + "'的用户不存在！");
		UserBean userBean = list.get(0);
		userDao.delete(userBean);
	}

	@Override
	public String modifyPwd(UserBean userBean) {
		// 根据userBean的ID查找用户
		UserBean u2 = userDao.findById(userBean.getUserId());
		// 如果用户不存在
		if(u2 == null){
			return "用户不存在";
		}
		// 获取新密码
		String password = userBean.getPassword();
		// 保存新密码
		u2.setPassword(password);
		userDao.saveOrUpdate(u2);
		return "修改成功";
	}
}
