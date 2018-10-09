package iqaa.xxzh.msl.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import iqaa.xxzh.common.bean.Role;
import iqaa.xxzh.msl.dao.RoleDao;

@Component(value="roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Autowired
	private RoleDao roleDao;



	@Override
	public void saveOrUpdate(Role role) {
		roleDao.saveOrUpdate(role);
	}



	@Override
	public List<Role> getAllRole() {
		DetachedCriteria c = DetachedCriteria.forClass(Role.class);
		List<Role> list = roleDao.findByCriteria(c, Role.class);
		return list;
	}
	
}
