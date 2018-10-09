package iqaa.xxzh.msl.bean;

import java.io.Serializable;
import java.util.Date;

import iqaa.xxzh.common.bean.UserBean;

public class DocBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String docName;
	private String path;
	/**
	 * 文件上传人
	 */
	private UserBean userBean;
	/**
	 * 文档上传时间
	 */
	private Date addtime;
	private String content;
	private String encoding;
	private Boolean isDelete;
	private String status = "00";
	private Integer mark = 0;
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	@Override
	public String toString() {
		return "DocBean [id=" + id + ", docName=" + docName + ", path=" + path + ", userBean=" + userBean + ", addtime="
				+ addtime + "]";
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
