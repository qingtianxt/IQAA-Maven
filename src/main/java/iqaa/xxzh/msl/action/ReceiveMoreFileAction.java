package iqaa.xxzh.msl.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.google.gson.JsonObject;
import com.mchange.io.FileUtils;
import com.opensymphony.xwork2.ActionSupport;

import iqaa.xxzh.common.bean.UserBean;
import iqaa.xxzh.msl.bean.DocBean;
import iqaa.xxzh.msl.service.DocService;
import iqaa.xxzh.msl.utils.Constant;
import iqaa.xxzh.msl.utils.FileUtil;

@Controller("receiveFileAction")
public class ReceiveMoreFileAction extends ActionSupport {
	
	
	@Override
	public String execute() throws Exception {
		/*String parentDir = ServletActionContext.getServletContext().getRealPath(savePath);
		File dir = new File(parentDir);
		if(!dir.exists()){
			dir.mkdirs();
		}*/
		String msg = null;
		List<DocBean> list = new ArrayList<>();
		try {
			// 循环遍历上传文件列表
			for(int i = 0 ; i < uploadFileName.size(); i++){
				String fileName = uploadFileName.get(i);
				File file = upload.get(i);
					String codeString = FileUtil.codeString(file);
					DocBean doc = new DocBean();
					String content = FileUtils.getContentsAsString(file, codeString);
					doc.setEncoding(codeString);
					doc.setContent(content);
					doc.setDocName(fileName);
					doc.setPath(savePath + "/" + fileName);
					UserBean user = (UserBean) ServletActionContext.getRequest().getSession()
							.getAttribute(Constant.SESSION_USER);
					doc.setUserBean(user);
					doc.setAddtime(new Date());
					doc.setIsDelete(false);
					doc.setStatus("00");
					list.add(doc);
			}
			docService.saveFile(list);
			
			
			
			msg = "保存成功!";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=utf-8");
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("code", 1);
		jsonObject.addProperty("msg", msg);
		jsonObject.add("data", new JsonObject());
		 response.getWriter().print(jsonObject.toString());
		return NONE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource(name="docService")
	private DocService docService;

	private List<String> uploadFileName;
	private List<File> upload;
	private List<String> uploadContentType;
	private String savePath;
	public List<String> getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(List<String> uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public List<File> getUpload() {
		return upload;
	}
	public void setUpload(List<File> upload) {
		this.upload = upload;
	}
	public List<String> getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(List<String> uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
}
