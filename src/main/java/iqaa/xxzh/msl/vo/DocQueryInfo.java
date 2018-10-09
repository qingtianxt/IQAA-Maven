package iqaa.xxzh.msl.vo;

import java.util.Date;

public class DocQueryInfo {

	private int currentPage = 1;
	private int pageSize = 5;
	private String param="";
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
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	@Override
	public String toString() {
		return "DocQueryInfo [currentPage=" + currentPage + ", pageSize=" + pageSize + ", param=" + param + ", start="
				+ start + ", end=" + end + "]";
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
