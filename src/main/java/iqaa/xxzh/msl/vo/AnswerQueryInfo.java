package iqaa.xxzh.msl.vo;

import java.io.Serializable;
import java.util.Date;

public class AnswerQueryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4747965765753435062L;
	public static final String SESSION_ANSERQUERYINFO = "sessionAnswerQueryInfo";
	private int currentPage = 1;
	private int pageSize = 5;
	private String answer="";
	private String question;
	private Date start;
	private Date end;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	@Override
	public String toString() {
		return "AnswerQueryInfo [currentPage=" + currentPage + ", pageSize=" + pageSize + ", answer=" + answer
				+ ", question=" + question + ", start=" + start + ", end=" + end + "]";
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
}
