package iqaa.xxzh.msl.vo;

public class UserQueryInfo {

	public static final String SEESSION_QUERY_INFO = "sessionUserQueryInfo";
	private Integer currentPage;
	private Integer pageSize;
	
	private String username;
	private String startTime;
	private String endTime;
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "UserQueryInfo [currentPage=" + currentPage + ", pageSize=" + pageSize + ", username=" + username
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
}
