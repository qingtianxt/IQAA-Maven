package iqaa.xxzh.msl.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.common.bean.Role;
import iqaa.xxzh.msl.service.RoleService;

@Controller("roleAction") @Scope("prototype")
public class RoleAction extends ActionSupport implements ModelDriven<Role>{

	private static final long serialVersionUID = 1L;

	public String toAddUser() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		
		List<Role> allRole = roleService.getAllRole();
		
		Gson gson = new Gson();
		String json = gson.toJson(allRole);
		
		PrintWriter out = response.getWriter();
		out.write(json);
		
		return NONE;
	}
	
	@Override
	public Role getModel() {
		return role;
	}

	private Role role = new Role();
	
	@Resource(name="roleService")
	private RoleService roleService;
}
