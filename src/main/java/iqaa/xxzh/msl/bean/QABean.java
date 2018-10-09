package iqaa.xxzh.msl.bean;

import java.io.Serializable;
import java.util.Date;

import iqaa.xxzh.common.bean.UserBean;

@SuppressWarnings("all")
public class QABean implements Serializable {

	private String id;
	private String s;
	private String answer;
	/**
	 * 主题
	 */
	private String subject;
	private String link;
	private DocBean doc;
	private UserBean user;
	private Date createTime;
	private Boolean isDelete;
	private Long clickRate;
	
}
