package iqaa.xxzh.msl.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.common.except.AnalyzedException;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.bean.Question;
import iqaa.xxzh.msl.service.DocService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.utils.FileUtil;
import iqaa.xxzh.msl.utils.HibernateProxyTypeAdapter;
import iqaa.xxzh.msl.vo.DocQueryInfo;
import iqaa.xxzh.msl.vo.PageBean;

@Controller @Scope("prototype")
public class DocumentAction extends ActionSupport implements ModelDriven<DocBean> {
	public String getJSONlist() throws IOException{
		getPage();
		Object object = ServletActionContext.getRequest().getAttribute(Constant.PAGEBEAN);
		GsonBuilder b = new GsonBuilder();  
		b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);  
		Gson gson = b.create();  
		String json = gson.toJson(object);
		ServletActionContext.getResponse().getWriter().print(json);
		return NONE;
	}
	public String getByRegulation() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		throw new Exception("该路劲作废，请访问"+request.getContextPath()+"/kb/selectquestion.action");
	}
	/**
	 * 从文件中提取QA对在页面显示
	 * @return
	 * @throws IOException 
	 */
	public String extract() throws IOException{
		JsonObject jsonObject = new JsonObject();
		try {
			// 获取文档信息
			doc = docService.getById(doc.getId());
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.removeAttribute("answeres");
			UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
			doc.setUserBean(user);
			// 分析获取结果
			List<Question> questionlst = docService.extract(doc);
			// 将结果转化为json数据发送
			GsonBuilder b = new GsonBuilder();  
			b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);  
			Gson gson = b.create();  
			jsonObject.addProperty("msg", "success");
			jsonObject.add("data", gson.toJsonTree(questionlst));
		} catch (AnalyzedException e) {
			e.printStackTrace();
			jsonObject.addProperty("msg", e.getMessage());
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(jsonObject);
		return NONE;
	}
	
	
	/**
	 * 从数据库中读取文档的内容，在页面显示
	 * @return
	 * @throws IOException
	 */
	public String display() throws IOException {
		// 获取文档的信息
		doc = docService.getById(doc.getId());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset="+doc.getEncoding());
		try {
			// 文档内容
			String content = doc.getContent();
			// 将文档内容输出到页面
			response.getWriter().println(content);
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("该文件不存在或者已经被删除！");
		}
		return NONE;
	}
	
	/**
	 * 下载文件
	 * @return
	 */
	public String download(){
		doc = docService.getById(doc.getId());
		return "download";
	}
	
	public String delete(){
		log.info("删除文件...."+doc.getId());
		HttpServletRequest request = ServletActionContext.getRequest();
		String tag = "deleteMsg";
		try {
			docService.delete(doc);
			request.setAttribute(tag, "删除成功！");
			log.info("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(tag, "删除失败！");
			log.info("删除失败！");
		}
		return "delete";
	}
	
	/**
	 * 分页显示文档信息
	 * @return
	 */
	public String getPage(){
		log.info("分页查询...");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(queryInfo == null){
			queryInfo = (DocQueryInfo) session.getAttribute(Constant.DOC_PAGINATION);
			if(queryInfo ==null){
				queryInfo = new DocQueryInfo();
				queryInfo.setCurrentPage(1);
				queryInfo.setPageSize(10);
				queryInfo.setParam("");
			}
		}
		session.setAttribute(Constant.DOC_PAGINATION, queryInfo);
		PageBean<DocBean> pages = docService.getList(queryInfo);
		request.setAttribute(Constant.PAGEBEAN, pages);
		log.info("查询成功...");
		return "show";
	}
	
	private DocQueryInfo queryInfo;
	@Deprecated
	public String upload(){
		log.info("文件上传...");
		log.info("file:"+getUpload());
		log.info("fileContextType:"+uploadContentType);
		log.info("fileFileName:"+getUploadFileName());
		String path = ServletActionContext.getServletContext().getRealPath(savePath);
		File target = new File(path);
		if(!target.exists()){
			target.mkdirs();
		}
		String uuidName = FileUtil.getUUIDName(uploadFileName);
		doc.setDocName(uploadFileName);
		doc.setPath(savePath+"/"+uuidName);
		
		File aim = new File(target,uuidName);
		boolean b = FileUtil.copy(upload, aim);
		
		if(b){
			UserBean user = (UserBean) ServletActionContext.getRequest().getSession().getAttribute(Constant.SESSION_USER);
			doc.setUserBean(user);
			doc.setAddtime(new Date());
			doc.setIsDelete(false);
			boolean c = docService.saveFile(doc);
			log.info("文档信息："+doc);
			if(c){
				addActionMessage("上传成功！");
				return "upload success";
			}else{
				this.addFieldError("uploadMsg", "文件保存到数据库失败！");
				aim.delete();
			}
		}else{
			addFieldError("uploadMsg", "写入文件失败！");
		}
		
		log.info("文件保存"+(b?"成功":"失败"));
		return "upload fail";
	}
	
	/*
	public String uploadMultiple(){
		
		return NONE;
	}*/
	
	private static Logger log = Logger.getLogger(DocumentAction.class);
	
	private static final long serialVersionUID = 1L;
	@Resource(name="docService")
	private DocService docService;
	public DocBean getModel() {
		return doc;
	}
	private DocBean doc = new DocBean();
	
	private String savePath;
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public DocQueryInfo getQueryInfo() {
		return queryInfo;
	}
	public void setQueryInfo(DocQueryInfo queryInfo) {
		this.queryInfo = queryInfo;
	}

	public InputStream getInputStream() throws IOException{
		String content = doc.getContent();
		byte[] bytes = content.getBytes(doc.getEncoding());
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		return bis;
	}
	
	public String getDownloadFileName(){
		String string = null;
		try {
			string = new String(doc.getDocName().getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	
}
