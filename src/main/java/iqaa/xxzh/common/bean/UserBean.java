package iqaa.xxzh.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserBean implements Serializable,HttpSessionBindingListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String username;
	private String password;
	private String salt;
	private Boolean isLock;
	private Role role;
	private Date addtime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	@Override
	public String toString() {
		return "UserBean [userId=" + userId + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", isLock=" + isLock + ", role=" + role + ", addtime=" + addtime + "]";
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Boolean getIsLock() {
		return isLock;
	}
	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		ServletContext context = session.getServletContext();
		Map<UserBean,HttpSession> map = (Map<UserBean, HttpSession>) context.getAttribute(USER_MAP);
		map.put(this, session);
		int size = map.size();
		System.out.println("当前在线人数："+size);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		HttpSession session = arg0.getSession();
		ServletContext context = session.getServletContext();
		Map<UserBean,HttpSession> map = (Map<UserBean,HttpSession>) context.getAttribute(USER_MAP);
		map.remove(this);
	}
	
	public static final String USER_MAP = "usermap";
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addtime == null) ? 0 : addtime.hashCode());
		result = prime * result + ((isLock == null) ? 0 : isLock.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBean other = (UserBean) obj;
		if (addtime == null) {
			if (other.addtime != null)
				return false;
		} else if (!addtime.equals(other.addtime))
			return false;
		if (isLock == null) {
			if (other.isLock != null)
				return false;
		} else if (!isLock.equals(other.isLock))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
