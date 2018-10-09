package iqaa.xxzh.common.bean;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	private String roleid;
	private String rolename;
	private Date addtime;
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	@Override
	public String toString() {
		return "Role [roleid=" + roleid + ", rolename=" + rolename + ", addtime=" + addtime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addtime == null) ? 0 : addtime.hashCode());
		result = prime * result + ((roleid == null) ? 0 : roleid.hashCode());
		result = prime * result + ((rolename == null) ? 0 : rolename.hashCode());
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
		Role other = (Role) obj;
		if (addtime == null) {
			if (other.addtime != null)
				return false;
		} else if (!addtime.equals(other.addtime))
			return false;
		if (roleid == null) {
			if (other.roleid != null)
				return false;
		} else if (!roleid.equals(other.roleid))
			return false;
		if (rolename == null) {
			if (other.rolename != null)
				return false;
		} else if (!rolename.equals(other.rolename))
			return false;
		return true;
	}
	
}
