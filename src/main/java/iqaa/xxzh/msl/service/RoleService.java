package iqaa.xxzh.msl.service;

import java.io.Serializable;
import java.util.List;

import iqaa.xxzh.common.bean.Role;

public interface RoleService extends Serializable {

	void saveOrUpdate(Role role);

	List<Role> getAllRole();

}
