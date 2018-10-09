package iqaa.xxzh.msl.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import iqaa.xxzh.common.bean.UserBean;

public class Answer implements Serializable {
 
	private static final long serialVersionUID = 1L;
	public static final String REQUEST_PAGEBEAN = "requestAnswer";
	private Long id;
	private String subject;		// 主题
	private String content;		// 内容
	private String link;		// 答案链接
	private Question standardQuestion;	// 标准问题
	private Set<Question> extendQuestion = new HashSet<>();// 扩展问题
	private Date addtime;	// 添加时间
	private UserBean operator;// 操作用户
	private DocBean doc;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Question getStandardQuestion() {
		return standardQuestion;
	}
	public void setStandardQuestion(Question standardQuestion) {
		this.standardQuestion = standardQuestion;
	}
	public Set<Question> getExtendQuestion() {
		return extendQuestion;
	}
	public void setExtendQuestion(Set<Question> extendQuestion) {
		this.extendQuestion = extendQuestion;
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
	@Override
	public String toString() {
		return "Answer [id=" + id + ", subject=" + subject + ", content=" + content + ", link=" + link + ", addtime="
				+ addtime + ", operator=" + operator + "]";
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
		Answer other = (Answer) obj;
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
