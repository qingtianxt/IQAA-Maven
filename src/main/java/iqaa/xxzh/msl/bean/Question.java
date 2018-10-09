package iqaa.xxzh.msl.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import iqaa.xxzh.common.bean.UserBean;

public class Question implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String subject;
	private String content;
	private Answer standardAnswer;
	private Set<Answer> relatedAnswer = new HashSet<>();
	private DocBean doc;
	private Date addtime;
	private UserBean operator;
	private Long clickRate;// 访问量
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Answer getStandardAnswer() {
		return standardAnswer;
	}
	public void setStandardAnswer(Answer standardAnswer) {
		this.standardAnswer = standardAnswer;
	}
	public Set<Answer> getRelatedAnswer() {
		return relatedAnswer;
	}
	public void setRelatedAnswer(Set<Answer> relatedAnswer) {
		this.relatedAnswer = relatedAnswer;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public UserBean getOperator() {
		return operator;
	}
	public void setOperator(UserBean operator) {
		this.operator = operator;
	}
	public Long getClickRate() {
		return clickRate;
	}
	public void setClickRate(Long clickRate) {
		this.clickRate = clickRate;
	}
	
	@Override
	public String toString() {
		return "Question [id=" + id + ", subject=" + subject + ", content=" + content + ", standardAnswer="
				+ standardAnswer + ", relatedAnswer=" + relatedAnswer + ", addtime=" + addtime + ", operator="
				+ operator + ", clickRate=" + clickRate + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public DocBean getDoc() {
		return doc;
	}
	public void setDoc(DocBean doc) {
		this.doc = doc;
	}
	
}
